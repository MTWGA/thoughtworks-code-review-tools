# Thoughtworks code review tools

[![Build](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/build.yml/badge.svg)](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/build.yml)
[![CodeQL](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/codeql-analysis.yml)
[![Release](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/release.yml/badge.svg)](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/release.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/17968.svg)](https://plugins.jetbrains.com/plugin/17968)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17968.svg)](https://plugins.jetbrains.com/plugin/17968)

## Template ToDo list

- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Get known with the [template documentation][template].
- [x] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml)
  and [sources package](/src/main/kotlin).
- [x] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [x] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate)
  for the first time.
- [x] Set the Plugin ID in the above README badges.
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified
  about releases containing new features and fixes.

<!-- Plugin description -->
开发本插件的目的是为 Code Review 提高工作效率，提高Code Review 的愉悦度，本软件近期会快速迭代更新，请及时更新至最新版本。 The purpose of developing this plug-in is to
improve the work efficiency of Code Review and the joy of Code Review. The software will be updated quickly in the near
future. Please update to the latest version in time.
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  ThoughtworksCodeReviewTools"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/tcpgnl/ThoughtworksCodeReviewTools/releases/latest) and install it
  manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template