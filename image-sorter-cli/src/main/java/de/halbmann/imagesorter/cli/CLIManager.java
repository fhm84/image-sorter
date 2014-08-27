package de.halbmann.imagesorter.cli;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLIManager {

	public static final char HELP = 'h';
	public static final char VERSION = 'v';
	public static final String LOG_FILE = "l";
	public static final String THREADS = "t";

	public static final String OUTPUT_DIR = "out";
	public static final String PREFIX = "pre";
	public static final String SUFFIX = "suf";
	public static final String OFFSET = "offset";
	public static final String RECURSIVE = "r";

	protected Options options;

	@SuppressWarnings("static-access")
	public CLIManager() {
		options = new Options();

		options.addOption(OptionBuilder.withLongOpt("help")
				.withDescription("Display help information").create(HELP));
		options.addOption(OptionBuilder.withLongOpt("version")
				.withDescription("Display version information").create(VERSION));

		// TODO: add (technical) options (Log-File, Threads, ...)

		options.addOption(OptionBuilder.withLongOpt("output-dir")
				.withDescription("Set the output directory for the modified images").hasArg()
				.create(OUTPUT_DIR));
		options.addOption(OptionBuilder.withLongOpt("prefix")
				.withDescription("Set the images prefix").hasArg().create(PREFIX));
		options.addOption(OptionBuilder.withLongOpt("suffix")
				.withDescription("Set the images suffix").hasArg().create(SUFFIX));
		options.addOption(OptionBuilder.withLongOpt("offset")
				.withDescription("Set the offset for numbering the images").withType(Number.class)
				.hasArg().create(OFFSET));
		options.addOption(OptionBuilder.withLongOpt("recursive")
				.withDescription("Process the directory recursivly").withType(Boolean.class)
				.hasArg().create(RECURSIVE));

		// TODO: add additional options for the settings?
	}

	public CommandLine parse(String[] args) throws ParseException {
		CommandLineParser parser = new GnuParser();

		return parser.parse(options, args);
	}

	public void displayHelp(PrintStream stdout) {
		stdout.println();

		PrintWriter pw = new PrintWriter(stdout);

		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH,
				"imgsort [command(s)] [options] [<setting(s)>]", "\nOptions:", options,
				HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, "\n", false);

		pw.flush();
	}

}