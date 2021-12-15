# Thoughtworks code review tools

[![Build](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/build.yml/badge.svg)](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/build.yml)
[![CodeQL](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/codeql-analysis.yml)
[![Release](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/release.yml/badge.svg)](https://github.com/tcpgnl/thoughtworks-code-review-tools/actions/workflows/release.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/17968.svg)](https://plugins.jetbrains.com/plugin/17968)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17968.svg)](https://plugins.jetbrains.com/plugin/17968)

<!-- Plugin description -->
The purpose of developing this plug-in is to improve the work efficiency of Code Review and the joy of Code Review. The
software will be updated quickly in the near future. Please update to the latest version in time.

开发本插件的目的是 Code Review 提高工作效率，本插件近期会快速迭代更新，请及时更新至最新版本。
<!-- Plugin description end -->

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
- Set up your trello configuration here:

  ![configuration](imgs/configuration.png)
  
- Trello key & token can be got here: [https://trello.com/app-key](https://trello.com/app-key)
  
- Board id can be found in url: ![boardId](./imgs/boardId.png)

- Due time will be added to card

## Setup Trello board

- Add members to the Trello board. Member should be displayed in this tool.

- Add labels to the trello board(menu -> label). Label is using for classifying problems we found in code review.

## How to use
  1. Select the code

  ![selectCode](./imgs/selectCoed.png)
  
  2. Call the tool by default shortcut: cmd + option + i

  3. Enter Owner, Label, feedback, then press enter
  
  ![codereviewTool](imgs/codeReviewPanel.png)

  4. A card will be created with the input information. Owner can click done when the problem is solved.

  4. Use refresh if a new member or label just be added in trello

---

## Contributors

<a href="https://github.com/tcpgnl/thoughtworks-code-review-tools/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=tcpgnl/thoughtworks-code-review-tools" />
</a>
