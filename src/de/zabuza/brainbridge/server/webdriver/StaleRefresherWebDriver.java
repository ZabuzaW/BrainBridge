package de.zabuza.brainbridge.server.webdriver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Wrapper for web driver objects to automatically handle staled elements that
 * would otherwise throw {@link StaleElementReferenceException}s when used.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class StaleRefresherWebDriver implements WebDriver, IWrapsWebDriver {

	/**
	 * Web driver to wrap for staled element handling.
	 */
	private final WebDriver mWebDriver;

	/**
	 * Creates a new instance of this object with a given web driver.
	 * 
	 * @param driver
	 *            Driver to wrap for staled element handling
	 */
	public StaleRefresherWebDriver(final WebDriver driver) {
		this.mWebDriver = driver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#close()
	 */
	@Override
	public void close() {
		this.mWebDriver.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
	 */
	@Override
	public WebElement findElement(final By by) {
		return new StaleRefresherWebElement(this.mWebDriver.findElement(by), by, this.mWebDriver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
	 */
	@Override
	public List<WebElement> findElements(final By by) {
		final List<WebElement> elements = this.mWebDriver.findElements(by);
		final List<WebElement> staleRefresherElements = new LinkedList<>();
		int i = 0;
		for (final WebElement element : elements) {
			staleRefresherElements.add(new StaleRefresherWebElement(element, by, this.mWebDriver, i));
			i++;
		}

		return staleRefresherElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#get(java.lang.String)
	 */
	@Override
	public void get(final String url) {
		this.mWebDriver.get(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#getCurrentUrl()
	 */
	@Override
	public String getCurrentUrl() {
		return this.mWebDriver.getCurrentUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#getPageSource()
	 */
	@Override
	public String getPageSource() {
		return this.mWebDriver.getPageSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.zabuza.sparkle.webdriver.IWrapsWebDriver#getRawDriver()
	 */
	@Override
	public WebDriver getRawDriver() {
		return this.mWebDriver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#getTitle()
	 */
	@Override
	public String getTitle() {
		return this.mWebDriver.getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#getWindowHandle()
	 */
	@Override
	public String getWindowHandle() {
		return this.mWebDriver.getWindowHandle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#getWindowHandles()
	 */
	@Override
	public Set<String> getWindowHandles() {
		return this.mWebDriver.getWindowHandles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#manage()
	 */
	@Override
	public Options manage() {
		return this.mWebDriver.manage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#navigate()
	 */
	@Override
	public Navigation navigate() {
		return this.mWebDriver.navigate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#quit()
	 */
	@Override
	public void quit() {
		this.mWebDriver.quit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.WebDriver#switchTo()
	 */
	@Override
	public TargetLocator switchTo() {
		return this.mWebDriver.switchTo();
	}

}
