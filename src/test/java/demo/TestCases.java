package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class TestCases {
    ChromeDriver driver;
    private Wrappers wrappers;
    private String baseUrl = "https://www.flipkart.com";

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        this.wrappers = new Wrappers(driver);
        driver.get(baseUrl);
        // try {
        // By closePopup = By.xpath("//button[contains(text(),'✕')]");
        // wrappers.click(closePopup);
        // } catch (Exception e) {
        // // Popup not present
        // }
    }

    @AfterTest
    public void endTest() {
        if (wrappers != null && wrappers.driver != null) {
            wrappers.driver.close();
            wrappers.driver.quit();
        }

    }

    @Test
    public void testCase01() throws InterruptedException {
        // Search for "Washing Machine"
        By searchBox = By.xpath("//input[@title='Search for Products, Brands and More']");
        Thread.sleep(3000);
        wrappers.enterText(searchBox, "Washing Machine");
        By searchButton = By.xpath("//button[@type='submit']");
        wrappers.click(searchButton);
        // Sort by Popularity
        By sortDropdown = By.xpath("//div[text()='Popularity']");
        wrappers.click(sortDropdown);

        // Retrieve all product ratings
        By ratingLocator = By.xpath("//div[@class='XQDdHH']");
        Thread.sleep(2000);
        List<WebElement> ratings = wrappers.getElements(ratingLocator);

        // Count items with rating <= 4 stars
        long count = ratings.stream()
                .map(WebElement::getText)
                .mapToDouble(Float::parseFloat)
                .filter(rating -> rating <= 4.0)
                .count();

        System.out.println("Count of items with rating <= 4 stars: " + count);
    }

    @Test
    public void testCase02() throws InterruptedException {
        try {
            // Search for "iPhone"

            By searchBox = By.xpath("//input[@title='Search for products, brands and more']");
            WebElement searchBoxElement = driver.findElement(searchBox);
            searchBoxElement.sendKeys(Keys.CONTROL, "a");
            searchBoxElement.sendKeys(Keys.DELETE);
            wrappers.enterText(searchBox, "iPhone");

            By searchButton = By.xpath("//button[@type='submit']");
            wrappers.click(searchButton);
            Thread.sleep(3000);

            // Retrieve all products with discounts
            By productLocator = By.xpath("//div[contains(@class, 'UkUFwK') and .//span[contains(text(), '%')]]");
            Thread.sleep(3000);
            List<WebElement> products = wrappers.getElements(productLocator);

            // Iterate through products and print titles and discounts > 17%
            for (WebElement product : products) {
                try {
                    // Get discount percentage
                    String discountText = product.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[6]/div/div/div/a/div[2]/div[2]/div[1]/div[1]/div[3]/span"))
                            .getText();
                    int discount = Integer.parseInt(discountText.replaceAll("[^0-9]", ""));

                    // Check discount > 17%
                    if (discount > 17) {
                        // Get title
                        String title = product.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[6]/div/div/div/a/div[2]/div[1]/div[1]")).getText();
                        System.out.println("Title: " + title);
                        System.out.println("Discount: " + discount + "%");
                    }
                } catch (Exception e) {
                    System.out.println("Error processing product: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Test failed due to error: " + e.getMessage());
        }
    }

    @Test
    public void testCase03() throws InterruptedException {
        // Search for "Coffee Mug"

        By searchBox = By.xpath("//input[@title='Search for products, brands and more']");
        WebElement searchBoxElement = driver.findElement(searchBox);
        searchBoxElement.sendKeys(Keys.CONTROL, "a");
        searchBoxElement.sendKeys(Keys.DELETE);
        wrappers.enterText(searchBox, "Coffee Mug");
        By searchButton = By.xpath("//button[@type='submit']");

        wrappers.click(searchButton);
        Thread.sleep(4000);

        // Filter by 4 stars and above
        By fourStarFilter = By
                .xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[1]/div/div/div/section[5]/div[2]/div/div[1]");
        Thread.sleep(5000);
        WebElement element = driver.findElement(fourStarFilter);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        wrappers.click(fourStarFilter);
        Thread.sleep(5000);

        // Retrieve all products with reviews
        By productLocator = By.xpath("//span[@class='Wphh3N']");
        Thread.sleep(3000);
        List<WebElement> products = wrappers.getElements(productLocator);

        // Map products to their review counts
        List<ProductReview> productReviews = products.stream().map(product -> {
            try {
                String title = product.findElement(By.xpath(".//div[contains(@class, 'WtfHe')]/a/div")).getText();
                String imageUrl = product.findElement(By.xpath(".//img")).getAttribute("src");
                String reviewText = product.findElement(By.xpath(".//span[contains(text(), 'reviews')]")).getText();
                int reviews = Integer.parseInt(reviewText.split(" ")[0].replaceAll(",", ""));
                return new ProductReview(title, imageUrl, reviews);
            } catch (Exception e) {
                return null;
            }
        }).filter(p -> p != null).collect(Collectors.toList());

        // Sort products by number of reviews in descending order
        productReviews.sort((p1, p2) -> Integer.compare(p2.getReviews(), p1.getReviews()));

        // Print top 5 products
        System.out.println("Top 5 Coffee Mugs with highest number of reviews:");
        productReviews.stream().limit(5).forEach(product -> {
            System.out.println("Title: " + product.getTitle());
            System.out.println("Image URL: " + product.getImageUrl());
            System.out.println("Number of Reviews: " + product.getReviews());
            System.out.println("---------------------------");
        });
    }

    // Helper class to store product details
    class ProductReview {
        private String title;
        private String imageUrl;
        private int reviews;

        public ProductReview(String title, String imageUrl, int reviews) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.reviews = reviews;
        }

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getReviews() {
            return reviews;
        }
    }
}
