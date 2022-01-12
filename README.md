# Thoughtworks code review tools

[![Build](https://github.com/MTWGA/thoughtworks-code-review-tools/actions/workflows/build.yml/badge.svg)](https://github.com/MTWGA/thoughtworks-code-review-tools/actions/workflows/build.yml)
[![CodeQL](https://github.com/MTWGA/thoughtworks-code-review-tools/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/MTWGA/thoughtworks-code-review-tools/actions/workflows/codeql-analysis.yml)
[![Release](https://github.com/MTWGA/thoughtworks-code-review-tools/actions/workflows/release.yml/badge.svg)](https://github.com/MTWGA/thoughtworks-code-review-tools/actions/workflows/release.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/17968.svg)](https://plugins.jetbrains.com/plugin/17968)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17968.svg)](https://plugins.jetbrains.com/plugin/17968)

<!-- Plugin description -->
The purpose of developing this plug-in is to improve the work efficiency of Code Review and the joy of Code Review.

开发本插件的目的是 Code Review 提高工作效率，插件需要和Trello配合使用，我们希望在code review中发现问题，在Trello中记录问题，并用Trello统计出现的问题，以此为方向给团队赋能。

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  ThoughtworksCodeReviewTools"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/MTWGA/thoughtworks-code-review-tools/releases/latest) and install it
  manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Configuration
- Set up your Trello configuration here:

  ![configuration](https://cdn.jsdelivr.net/gh/MTWGA/thoughtworks-code-review-tools@master/imgs/configuration.png)

- Trello key & token can be got here: [https://trello.com/app-key](https://trello.com/app-key)
  trello key and token: ![trelloKeyAndToken](https://cdn.jsdelivr.net/gh/Afaren/thoughtworks-code-review-tools@master/imgs/trelloKeyAndToken.png)

- Board id can be found in
  url: ![boardId](https://cdn.jsdelivr.net/gh/MTWGA/thoughtworks-code-review-tools@master/imgs/boardID.png)

- Due time will be added to card for marking as done when problem solved

## Setup Trello board

- Add members to the Trello board. Member should be displayed in this tool

- Add labels to the Trello board(menu -> label). Label is using for classifying problems found in code review

## How to use
  1. Select the code

![selectCode](https://cdn.jsdelivr.net/gh/MTWGA/thoughtworks-code-review-tools@master/imgs/selectCoed.png)
  
  2. Call the tool by default shortcut: cmd + option + i

  3. Enter Owner, Label, feedback, then press enter

![codereviewTool](https://cdn.jsdelivr.net/gh/MTWGA/thoughtworks-code-review-tools@master/imgs/codeReviewPanel.png)

  4. A notification will display if the Trello card created

![receipt](https://cdn.jsdelivr.net/gh/MTWGA/thoughtworks-code-review-tools@master/imgs/receipt.png)


## Refresh

- Use refresh button if a new member or label just be added to Trello board

<!-- Plugin description end -->

---

## Contributors

<a href="https://github.com/MTWGA/thoughtworks-code-review-tools/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=MTWGA/thoughtworks-code-review-tools" />
</a>
