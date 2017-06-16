package org.springframework.samples.petclinic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestPetClinicChrome {
	public WebDriver driver;
	public String URL, Node;
	protected ThreadLocal<RemoteWebDriver> threadDriver = null;

	@Parameters(value={"url","seleniumGridUrl"})
	@BeforeTest
	public void launchbrowser(String url, String seleniumGridUrl) throws MalformedURLException {
		String browser = "chrome";
		String URL = url;
		if (browser.equalsIgnoreCase("firefox")) {
			System.out.println(" Executing on FireFox at grid "+ seleniumGridUrl);
			String Node = seleniumGridUrl;
			DesiredCapabilities cap = DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			driver = new RemoteWebDriver(new URL(Node), cap);
			// Puts an Implicit wait, Will wait for 10 seconds before throwing
			// exception
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// Launch website
			driver.navigate().to(URL);
			driver.manage().window().maximize();
		} else if (browser.equalsIgnoreCase("chrome")) {
			System.out.println(" Executing on CHROME at grid "+ seleniumGridUrl);
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			String Node = seleniumGridUrl;
			driver = new RemoteWebDriver(new URL(Node), cap);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// Launch website
			driver.navigate().to(URL);
			driver.manage().window().maximize();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.out.println(" Executing on IE at grid "+seleniumGridUrl);
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setBrowserName("ie");
//			String Node = "http://192.168.99.100:32768/wd/hub";
			String Node = seleniumGridUrl;
			driver = new RemoteWebDriver(new URL(Node), cap);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// Launch website
			driver.navigate().to(URL);
			driver.manage().window().maximize();
		} else {
			throw new IllegalArgumentException("The Browser Type is Undefined");
		}
		
		System.out.println("Remote session Id: "+ ((RemoteWebDriver) driver).getSessionId());
	}

	@Test(groups = { "functionaltest"})
	public void createOwnerAndVerify() {
		
		long random= System.currentTimeMillis();
		System.out.println("Running the find owner test on chrome");
		System.out.println("Random value: "+random);
		
		System.out.println("Waiting for page to load");
		waitForLoad(driver);
		
		
		System.out.println("Page loaded proceeding with the test");
		// Click on find owners
		driver.findElement(By.xpath("//a[@title='find owners']")).click();
		
		// Click on add owners
		driver.findElement(By.xpath("//a[contains(text(),'Add Owner')]")).click();
		driver.findElement(By.id("firstName")).sendKeys("qubefirstname-"+random);
		driver.findElement(By.id("lastName")).sendKeys("qubelastname-"+random);
		driver.findElement(By.id("address")).sendKeys("freedom circle-"+random);
		driver.findElement(By.id("city")).sendKeys("santa clara-"+random);
		driver.findElement(By.id("telephone")).sendKeys("4084084084");
		
		// submit
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		// Click on find owners page
		driver.findElement(By.xpath("//a[@title='find owners']")).click();
		// Click on find owner button
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		//lookup for newly added firstname and lastname in table (td)
		driver.findElement(By.xpath("//td[contains(text(),qubefirstname-"+random+")]"));
		driver.findElement(By.xpath("//td[contains(text(),qubelastname-"+random+")]"));
	}
	
	
	public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 45);
        wait.until(pageLoadCondition);
    }

	@AfterTest
	public void closeBrowser() {
		 driver.quit();
	}
}