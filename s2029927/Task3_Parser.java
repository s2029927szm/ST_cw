package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private OptionMap optionMap;
	
	public Parser() {
		optionMap = new OptionMap();
	}
	
	public void addOption(Option option, String shortcut) {
		optionMap.store(option, shortcut);
	}
	
	public void addOption(Option option) {
		optionMap.store(option, "");
	}
	
	public boolean optionExists(String key) {
		return optionMap.optionExists(key);
	}
	
	public boolean shortcutExists(String key) {
		return optionMap.shortcutExists(key);
	}
	
	public boolean optionOrShortcutExists(String key) {
		return optionMap.optionOrShortcutExists(key);
	}
	
	public int getInteger(String optionName) {
		String value = getString(optionName);
		Type type = getType(optionName);
		int result;
		switch (type) {
			case STRING:
			case INTEGER:
				try {
					result = Integer.parseInt(value);
				} catch (Exception e) {
			        try {
			            new BigInteger(value);
			        } catch (Exception e1) {
			        }
			        result = 0;
			    }
				break;
			case BOOLEAN:
				result = getBoolean(optionName) ? 1 : 0;
				break;
			case CHARACTER:
				result = (int) getCharacter(optionName);
				break;
			default:
				result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String optionName) {
		String value = getString(optionName);
		return !(value.toLowerCase().equals("false") || value.equals("0") || value.equals(""));
	}
	
	public String getString(String optionName) {
		return optionMap.getValue(optionName);
	}
	
	public char getCharacter(String optionName) {
		String value = getString(optionName);
		return value.equals("") ? '\0' :  value.charAt(0);
	}
	
	public void setShortcut(String optionName, String shortcutName) {
		optionMap.setShortcut(optionName, shortcutName);
	}
	
	public void replace(String variables, String pattern, String value) {
			
		variables = variables.replaceAll("\\s+", " ");
		
		String[] varsArray = variables.split(" ");
		
		for (int i = 0; i < varsArray.length; ++i) {
			String varName = varsArray[i];
			String var = (getString(varName));
			var = var.replace(pattern, value);
			if(varName.startsWith("--")) {
				String varNameNoDash = varName.substring(2);
				if (optionMap.optionExists(varNameNoDash)) {
					optionMap.setValueWithOptionName(varNameNoDash, var);
				}
			} else if (varName.startsWith("-")) {
				String varNameNoDash = varName.substring(1);
				if (optionMap.shortcutExists(varNameNoDash)) {
					optionMap.setValueWithOptionShortcut(varNameNoDash, var);
				} 
			} else {
				if (optionMap.optionExists(varName)) {
					optionMap.setValueWithOptionName(varName, var);
				}
				if (optionMap.shortcutExists(varName)) {
					optionMap.setValueWithOptionShortcut(varName, var);
				} 
			}

		}
	}
	
	private List<CustomPair> findMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    List<CustomPair> pairs = new ArrayList<CustomPair>();
	    while (matcher.find()) {
	    	CustomPair pair = new CustomPair(matcher.start(), matcher.end());
	    	pairs.add(pair);
	    }
	    return pairs;
	}
	
	
	public int parse(String commandLineOptions) {
		if (commandLineOptions == null) {
			return -1;
		}
		int length = commandLineOptions.length();
		if (length == 0) {
			return -2;
		}	
		
		List<CustomPair> singleQuotePairs = findMatches(commandLineOptions, "(?<=\')(.*?)(?=\')");
		List<CustomPair> doubleQuote = findMatches(commandLineOptions, "(?<=\")(.*?)(?=\")");
		List<CustomPair> assignPairs = findMatches(commandLineOptions, "(?<=\\=)(.*?)(?=[\\s]|$)");
		
		
		for (CustomPair pair : singleQuotePairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
	    	
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);
		}
		
		for (CustomPair pair : doubleQuote) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\'", "{S_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
			
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}
		
		for (CustomPair pair : assignPairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll("\'", "{S_QUOTE}").
					  replaceAll("-", "{DASH}");
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}

		commandLineOptions = commandLineOptions.replaceAll("--", "-+").replaceAll("\\s+", " ");


		String[] elements = commandLineOptions.split("-");
		
		
		for (int i = 0; i < elements.length; ++i) {
			String entry = elements[i];
			
			if(entry.isBlank()) {
				continue;
			}

			String[] entrySplit = entry.split("[\\s=]", 2);
			
			boolean isKeyOption = entry.startsWith("+");
			String key = entrySplit[0];
			key = isKeyOption ? key.substring(1) : key;
			String value = "";
			
			if(entrySplit.length > 1 && !entrySplit[1].isBlank()) {
				String valueWithNoise = entrySplit[1].trim();
				value = valueWithNoise.split(" ")[0];
			}
			
			// Explicitly convert boolean.
			if (getType(key) == Type.BOOLEAN && (value.toLowerCase().equals("false") || value.equals("0"))) {
				value = "";
			}
			
			value = value.replace("{S_QUOTE}", "\'").
						  replace("{D_QUOTE}", "\"").
						  replace("{SPACE}", " ").
						  replace("{DASH}", "-").
						  replace("{EQUALS}", "=");
			
			
			boolean isUnescapedValueInQuotes = (value.startsWith("\'") && value.endsWith("\'")) ||
					(value.startsWith("\"") && value.endsWith("\""));
			
			value = value.length() > 1 && isUnescapedValueInQuotes ? value.substring(1, value.length() - 1) : value;
			
			if(isKeyOption) {
				optionMap.setValueWithOptionName(key, value);
			} else {
				optionMap.setValueWithOptionShortcut(key, value);
				
			}			
		}

		return 0;
		
	}

	
	private Type getType(String option) {
		Type type = optionMap.getType(option);
		return type;
	}
	
	@Override
	public String toString() {
		return optionMap.toString();
	}

	
	private class CustomPair {
		
		CustomPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	    private int x;
	    private int y;
	    
	    public int getX() {
	    	return this.x;
	    }
	    
	    public int getY() {
	    	return this.y;
	    }
	}

    public void addAllByBulk(String options, String types) {
        addAllByBulk(options, "", types);
    }

    public void addAllByBulk(String options, String shortcuts, String types) {
        options = options.replaceAll("\\s+", " ");
        shortcuts = shortcuts.replaceAll("\\s+", " ");
        types = types.replaceAll("\\s+", " ");

        String[] optsArray = options.split(" ");
        String[] shortcutsArray = shortcuts.split(" ");
        String[] typesArray = types.split(" ");

        for (int i = 0; i < optsArray.length; i++) {
            String option = optsArray[i];
            String shortcut = i < shortcutsArray.length ? shortcutsArray[i] : "";
            String type = i < typesArray.length ? typesArray[i] : typesArray[typesArray.length - 1];

            Type typeEnum = Type.valueOf(type);
            addOption(new Option(option, typeEnum), shortcut);
        }
    }


    private static final String REGEX = "(([A-Za-z0-9_])+(([A-Z]-[A-Z]{1})|[a-z]-[a-z]{1}|[0-9]-[0-9]+))";

    public void matcherParam(String optGroup, List<String> optArray) {
        Matcher matcher = Pattern.compile(REGEX).matcher(optGroup);
        boolean isMatch = false;
        if (matcher.find()) {
            if (matcher.start() == 0 && matcher.end() == optGroup.length()) {
                isMatch = true;
            }
        }

        if (!isMatch) {
            optArray.add(optGroup);
            return;
        }

        //valid parameters
        String[] optGroupArray = optGroup.split("-", 2);
        String group = optGroupArray[0].substring(0, optGroupArray[0].length() - 1);
        String begin = optGroupArray[0].substring(optGroupArray[0].length() - 1);
        String end = optGroupArray[1];

        boolean isNumber = begin.matches("[0-9]");

        int beginInt = isNumber? Integer.valueOf(begin) : begin.charAt(0);
        int endInt = isNumber ? Integer.valueOf(end) : end.charAt(0);
        if (beginInt < endInt) {
            for (int i = beginInt; i <= endInt; i++) {
                optArray.add(group + (isNumber ? i : String.valueOf((char) i)));
            }
        } else {
            for (int i = beginInt; i >= endInt; i--) {
                optArray.add(group + (isNumber ? i : String.valueOf((char) i)));
            }
        }
    }

    public void addAll(String options, String types) {
        addAll(options, "", types);
    }

    public void addAll(String options, String shortcuts, String types) {
        options = options.replaceAll("\\s+", " ");
        shortcuts = shortcuts.replaceAll("\\s+", " ");
        types = types.replaceAll("\\s+", " ");

        // space-separated options
        String[] optGroupArray = options.split(" ");
        List<String> optArray = new ArrayList<String>();

        String[] typeGroupArray = types.split(" ");
        List<String> typeArray = new ArrayList<String>();

        for (int i = 0; i < optGroupArray.length; i++) {
            // matched group
            matcherParam(optGroupArray[i], optArray);
            String type = i < typeGroupArray.length ?
                    typeGroupArray[i] : typeGroupArray[typeGroupArray.length - 1];
            for (int j = typeArray.size(); j < optArray.size(); j++) {
                typeArray.add(type);
            }
        }

        // space-separated shortcuts
        String[] scGroupArray = shortcuts.split(" ");
        List<String> scArray = new ArrayList<String>();
        for (String scGroup : scGroupArray) {
            matcherParam(scGroup, scArray);
        }


        for (int i = 0; i < optArray.size(); i++) {
            String option = optArray.get(i);
            String shortcut = i < scArray.size() ? scArray.get(i) : "";
            String type = typeArray.get(i);

            Type typeEnum;
            try {
                typeEnum = Type.valueOf(type);
            } catch (Exception e) {
                throw new IllegalArgumentException("type error");
            }

            addOption(new Option(option, typeEnum), shortcut);
        }
    }
}
