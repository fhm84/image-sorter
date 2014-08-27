package de.halbmann.imagesorter.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import de.halbmann.imagesorter.ImageSorterExecution;
import de.halbmann.imagesorter.ImageSorterSettings;

/**
 * The ImageSorterCli is the main entry point to start the image sorter from the console.
 * 
 * @author fabian
 * 
 */
public class ImageSorterCli {

	private static final String CONFIG_DIR = "config/";

	static {
		System.setProperty("java.util.logging.config.file", CONFIG_DIR + "logging.properties");
	}

	/**
	 * Main entry point to start the image sorter from the console.
	 * 
	 * @param args
	 *            the applications arguments: the first one will be the current directory (to order
	 *            the images in), the second should be the "run" command and the rest will be some
	 *            optional settings
	 */
	public static void main(String[] args) {
		ImageSorterCli cli = new ImageSorterCli();
		cli.execute(args, System.out);
	}

	public void execute(String[] args, PrintStream stdout) {
		CLIManager cliManager = new CLIManager();

		try {
			CommandLine cmd = cliManager.parse(args);

			if (cmd.hasOption(CLIManager.HELP)) {
				cliManager.displayHelp(stdout);
			}

			if (cmd.hasOption(CLIManager.VERSION)) {
				// TODO: show version!
			}

			if (cmd.getArgs().length >= 2) {
				String baseDir = cmd.getArgs()[0];
				String runCommand = cmd.getArgs()[1];

				checkDirectory(baseDir);

				// TODO: optimize this!
				if ("run".equalsIgnoreCase(runCommand)) {
					ImageSorterSettings settings = prepareSettings(cmd);

					stdout.println(String.format("baseDir: %s", baseDir));

					ImageSorterExecution execution = new ImageSorterExecution(baseDir, settings);
					execution.run();
				} else {
					throw new RuntimeException("'run' command expected!");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace(stdout);
		} catch (IOException e) {
			e.printStackTrace(stdout);
		}
	}

	private boolean checkDirectory(String baseDir) {
		if (!Files.exists(Paths.get(baseDir))) {
			throw new RuntimeException("Path doesn't exist!");
		}
		// check the directory for image-files here?
		return true;
	}

	private ImageSorterSettings prepareSettings(CommandLine cmd) {
		// TODO: add the individual ones from the commandline
		ImageSorterSettings settings = readSettingsFile();

		if (cmd.hasOption(CLIManager.OUTPUT_DIR)) {
			settings.setOutputDir(cmd.getOptionValue(CLIManager.OUTPUT_DIR));
		}
		if (cmd.hasOption(CLIManager.PREFIX)) {
			System.out.println(cmd.getOptionProperties(CLIManager.PREFIX));
			settings.setPrefix(cmd.getOptionValue(CLIManager.PREFIX));
		}
		if (cmd.hasOption(CLIManager.SUFFIX)) {
			settings.setSuffix(cmd.getOptionValue(CLIManager.SUFFIX));
		}
		if (cmd.hasOption(CLIManager.OFFSET)) {
			try {
				Object offset = cmd.getParsedOptionValue(CLIManager.OFFSET);
				if (offset != null) {
					settings.setOffset(((Long) offset).intValue());
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (cmd.hasOption(CLIManager.RECURSIVE)) {
			try {
				settings.setRecursive((Boolean) cmd.getParsedOptionValue(CLIManager.RECURSIVE));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return settings;
	}

	private ImageSorterSettings readSettingsFile() {
		// read the "common"/global settings
		Config config = ConfigFactory.load(CONFIG_DIR + "application.conf");
		System.out.println(config.getAnyRef("imagesorter"));

		// TODO: map the properties to the settings

		return new ImageSorterSettings();
	}
}
