# Java QA Automation Demo

This repository contains a QA automation demo using Java, Maven, Selenium WebDriver, and TestNG. The project demonstrates how to automate e-commerce workflow on Rozetka website.

## Project Structure

The automation framework is designed using Page Object Model (POM) design pattern, and tests are written using the TestNG framework. The page-specific operations are abstracted out in the classes under the `com.os8580.pages` package.

## Setup

### Prerequisites

* Java JDK 17
* Maven

### Running the Test Suite

The repository contains a `pom.xml` file, which is a project configuration file for Maven. You can use Maven to run the test suite using the `mvn test` command in the terminal.

\`\`\`bash
mvn test
\`\`\`

### Project Structure

* `RozetkaTestSuite` - This class under the `com.os8580` package contains the test suite that will be run. It includes a `setupDriver` method for setting up the Chrome WebDriver before executing the test suite, and a `tearDown` method for closing down the WebDriver after executing the test suite. The test method `testRozetka` contains the actual testing logic.

* `com.os8580.pages` - This package contains the `AbstractPage`, `GooglePage`, `RozetkaPage` classes representing the corresponding web pages and providing methods for interacting with page elements. For example, `RozetkaPage` class includes methods to set search value, perform search, get search results, add product to cart, increase/decrease product quantity, and more.

* `com.os8580.utils` - This package contains the `PageFactory` class that returns the instances of `GooglePage`, `RozetkaPage` and other potential pages.

* `GooglePage`, `RozetkaPage`- The pages are modeled as classes, derived from an abstract base class `AbstractPage`. These classes encapsulate the functionality of the respective web pages.

* `Pages` - An enumeration, representing different page types, such as GOOGLE and ROZETKA.

* `testng.xml` - The TestNG configuration file. It includes the list of classes to run as part of the test suite.

* `pom.xml` - The Maven project configuration file.

## Note

This is a demo project intended only to showcase automation capabilities using Selenium WebDriver, Java, and TestNG. Any eCommerce transactions or operations performed are purely simulated and do not correspond to real purchases.
