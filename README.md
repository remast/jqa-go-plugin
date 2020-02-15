# jQAssistant Go Plugin

This is a **Go** parser for [jQAssistant](https://jqassistant.org/). 
It enables jQAssistant to scan and to analyze **[Go](https://golang.org/)** files.

## Getting Started

Download the jQAssistant command line tool for your system: [jQAssistant - Get Started](https://jqassistant.org/get-started/).

Next download the latest version from the release tab. Put the `jqa-plsql-plugin-*.jar` into the plugins 
folder of the jQAssistant commandline tool.

Now scan your files and wait for the plugin to finish:

```bash
jqassistant.sh scan -f example.go
```

You can then start a local Neo4j server to start querying the database at [http://localhost:7474](http://localhost:7474):

```bash
jqassistant.sh server
```

## Credits
Go Grammar provided by https://github.com/antlr/grammars-v4.