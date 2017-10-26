import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandPrompt {
	private final Map<String, Command> commands;
	private boolean shouldStop;
	
	public CommandPrompt() {
		commands = new HashMap<String, Command>();
	}
	
	/**
	 * Source from:
	 * https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
	 */
	public List<String> getTokens(String l) {
		List<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(l);
		while (m.find())
		    list.add(m.group(1).replace("\"", "")); // Added .replace("\"", "") to remove surrounding quotes.
		return list;
	}
	
	public void run(Scanner scan) {
		shouldStop = false;
		while(!shouldStop) {
			String cmd = scan.nextLine();
			String cmdName = cmd.split(" ")[0].toLowerCase();
			if(commands.containsKey(cmdName)) {
				commands.get(cmdName).function.run(cmd);
			}
			else {
				
			}
		}
	}
	
	public void stop() {
		shouldStop = true;
	}
	
	public void addCommand(Command c) {
		this.commands.put(c.name.toLowerCase(), c);
	}

	public void addCommand(String name, CommandFunction func, String description) {
		addCommand(new Command(name, func, description));
	}

	public void addCommand(String name, CommandFunction func) {
		addCommand(new Command(name, func));
	}
	
	public Map<String, Command> getCommands() {
		return commands;
	}
	
	@FunctionalInterface
	public static interface CommandFunction {
		void run(String line);
	}
	
	public class Command {
		public final String name;
		public final CommandFunction function;
		public String description;
		
		public Command(String name, CommandFunction f, String description) {
			this.name = name;
			this.function = f;
			this.description = description;
		}
		
		public Command(String name, CommandFunction f) {
			this.name = name;
			this.function = f;
			this.description = "No description";
		}
	}
	
}
