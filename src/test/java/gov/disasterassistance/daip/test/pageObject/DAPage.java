package gov.disasterassistance.daip.test.pageObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gov.disasterassistance.daip.test.exceptions.BenefitCountException;
import gov.disasterassistance.daip.test.exceptions.EmploymentException;
import gov.disasterassistance.daip.test.exceptions.FeedException;
import gov.disasterassistance.daip.test.exceptions.LocalResourcesException;
import gov.disasterassistance.daip.test.exceptions.StateException;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

/*************************************************************************
 * Using Selenium Webdriver, this class handles web related code such as pulling
 * elements from the given site.
 *
 * @author Chris Viqueira
 *************************************************************************/
@DefaultUrl("http://www.disasterassistance.gov")
public class DAPage extends PageObject {

	public DAPage(WebDriver driver) {
		super(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
	}

	public void clearCookies() {
		this.getDriver().manage().deleteAllCookies();
	}


	// *************************************************************************
	// FindBy / private variables section

	@FindBy(id = "stateSelector")
	private WebElementFacade stateSelector;

	@FindBy(xpath = "//fieldset//label[@class[contains(., 'radio')] and text()[not(contains(., 'No')) and not(contains(., 'Unknown'))]]")
	private List<WebElementFacade> questionnaireButtons;

	@FindBy(xpath = "//div[@class[contains(., 'accordion') and not(contains(., 'name'))]]")
	private List<WebElementFacade> accordionBlocks;

	@FindBy(xpath = "//*[@id='pageContent']")
	private WebElementFacade checkStatusPageContent;

	@FindBy(xpath = "//a[@href[contains(., 'TextCaptcha')]]")
	private WebElementFacade textCaptcha;

	@FindBy(xpath = "//div[@id='block-daip-responsive-questionnaire-responsive-questionnaire-block']")
	private WebElementFacade questionnaire;

	@FindBy(xpath = "//*//div[@id='address-lookup-container']")
	private WebElementFacade addressLookup;

	@FindBy(xpath = "//div[@class='foa-result-wrapper auto-foas']/div//h2/span[not(@class='chevron')]")
	private List<WebElementFacade> autoFOAs;

	@FindBy(xpath = "//div[@class='foaccordionable']")
	private List<WebElementFacade> FOAResults;

	@FindBy(xpath = "//div[@class='foaccordionable open']")
	private List<WebElementFacade> FOAExpandedResults;

	@FindBy(xpath = "//div[@class='foatoolbar-plusminus']")
	private WebElementFacade expandAllButton;

	@FindBy(xpath = "//div[@class='foatoolbar-minus']")
	private WebElementFacade collapseAllButton;

	@FindBy(id = "benefit-counter-count")
	private WebElementFacade benefitCounter;

	@FindBy(xpath = "//*[@class='page__title title']")
	private WebElementFacade pageTitle;

	@FindBy(xpath = "//ul[@class='expand-collapse']/li")
	List<WebElementFacade> federalAgencyAccordions;

	@FindBy(xpath = "//nav[@id='nav']/ul/li[@class[contains(., 'menu__item')]]")
	private List<WebElementFacade> navParentNode;

	@FindBy(xpath = "//div[@id='landing-page-container']/a")
	private List<WebElementFacade> landingPageNode;

	@FindBy(xpath = "//*[@class='state selected single-state-group']")
	private List<WebElementFacade> disasterStates;

	@FindBy(xpath = "//*[@class='state']")
	private List<WebElementFacade> greyStates;

	@FindBy(xpath = "//*[@id='svg2']")
	private WebElementFacade disasterMap;

	@FindBy(xpath = "//*[@class='timeline-TweetList-tweet customisable-border']")
	private List<WebElementFacade> twitterFeed;

	@FindBy(xpath = "//*[@id='block-twitter-block-1']")
	private WebElementFacade twitterFeedBlock;

	@FindBy(xpath = "//*[@id=\"local-resources-input\"]")
	private WebElementFacade localResourcesTextbox;

	@FindBy(xpath = "//*[@class='location-item clickable']")
	private List<WebElementFacade> localResourcesResults;

	@FindBy(xpath = "//button[@title='Next']")
	private WebElementFacade nextButtonFOA;

	@FindBy(xpath = "//button[@title='Back ']")
	private WebElementFacade backButtonFOA;
	
	@FindBy(xpath = "//div[@id='qtr-footer-section']/div")
	private List<WebElementFacade> FOAfooter;
	
	@FindBy(xpath = "//div[@class='checklist-accordion-button']")
	private List<WebElementFacade> FOAChecklistAccordions;

	private List<WebElementFacade> allElements = new ArrayList<WebElementFacade>();

	// *************************************************************************
	// Functions

	/*************************************************************************
	 * Searches through the main menu nodes and current landing page nodes to
	 * find the element with the same name that was passed. Once it finds the
	 * node it will click on the button.
	 * 
	 * @param node
	 *            : Name of the node to be clicked on
	 *************************************************************************/
	public void clickNavNode(String node) {
		allElements.clear();
		allElements.addAll(landingPageNode);
		allElements.addAll(navParentNode);

		Iterator<WebElementFacade> iter = allElements.iterator();
		WebElementFacade element = null;
		while (iter.hasNext()) {
			WebElementFacade tempElement = iter.next();
			String tempTitle = tempElement.getText();

			if (tempTitle.toLowerCase().contains(node)) {
				element = tempElement;
				break;
			}
		}

		element.click();
	}

	/*************************************************************************
	 * Completes the entire FOA questionnaire checking every box, saying yes to
	 * every question, and picking a state.
	 * 
	 *************************************************************************/
	public void completeFullQuestionnaire() {
		Iterator<WebElementFacade> iter = questionnaireButtons.iterator();
		while (iter.hasNext()) {
			WebElementFacade temp = iter.next();
			temp.click();
			this.evaluateJavascript("window.scrollBy(0,50)");
		}
		stateSelector.sendKeys("Alabama");

	}

	/*************************************************************************
	 * Clicks an individual element from the list of all of the questionnaire
	 * buttons.
	 * 
	 * @param buttonName
	 *            : The name of the element to be clicked
	 *************************************************************************/
	// TODO still doesn't distinguish between the yes buttons, just looks at the
	// text by the clickable button
	private WebElementFacade getQuestionnaireButton(String buttonName) {
		Iterator<WebElementFacade> iter = questionnaireButtons.iterator();
		WebElementFacade btn = null;
		while (iter.hasNext()) {
			WebElementFacade temp = iter.next();
			if (temp.containsText(buttonName)) {
				btn = temp;
				break;
			}
		}
		return btn;
	}

	/*************************************************************************
	 * Returns the element that correlates with the parameter provided from the
	 * list of all of the questionnaire buttons.
	 * 
	 * @param federalAgencyName
	 *            : The name of the agency to be returned
	 *************************************************************************/
	public WebElementFacade getFederalAgency(String federalAgencyName) {
		Iterator<WebElementFacade> iter = federalAgencyAccordions.iterator();
		WebElementFacade accordion = null;
		while (iter.hasNext()) {
			WebElementFacade temp = iter.next();
			if (temp.containsText(federalAgencyName)) {
				accordion = temp;
				break;
			}
		}
		return accordion;
	}

	/*************************************************************************
	 * Iterates through each Federal Agency and verifies that the number of
	 * benefits displayed correlates with its respective benefit counter.
	 * 
	 *************************************************************************/
	public void checkFederalBenefits() throws BenefitCountException {
		Iterator<WebElementFacade> federalAgencyIter = federalAgencyAccordions.iterator();
		while (federalAgencyIter.hasNext()) {
			WebElementFacade federalAgency = federalAgencyIter.next();
			String departmentNameAndBenefits = federalAgency.getText();
			String departmentName = departmentNameAndBenefits.substring(0, departmentNameAndBenefits.length() - 2)
					.trim();
			int benefitCount = Integer
					.parseInt(departmentNameAndBenefits.substring(departmentNameAndBenefits.length() - 2).trim());

			List<WebElement> benefits = this.getDriver().findElements(By.xpath("//div[@class='accordion-name-text' and "
					+ "text()[contains(., '" + departmentName + "')]]/../../ul//li"));

			if (benefits.size() != benefitCount) {
				throw new BenefitCountException("Incorrect number of benefits");
			}
		}
	}

	/*************************************************************************
	 * Checks if all states with disasters are clickable on the disaster map and
	 * if all other states are visible.
	 * 
	 *************************************************************************/
	public void checkStates() throws StateException {
		Iterator<WebElementFacade> disasterStateIter = disasterStates.iterator();
		Iterator<WebElementFacade> greyStateIter = greyStates.iterator();
		while (disasterStateIter.hasNext()) {
			WebElementFacade state = disasterStateIter.next();
			if (!state.isEnabled()) {
				throw new StateException("Unclickable state.");
			}
		}
		while (greyStateIter.hasNext()) {
			WebElementFacade state = greyStateIter.next();
			if (!state.isVisible()) {
				throw new StateException("State not visible.");
			}
		}
	}

	/*************************************************************************
	 * Verifies that the disaster map is visible on the homepage.
	 * 
	 *************************************************************************/
	public void checkDisasterMap() throws StateException {
		if (!disasterMap.isVisible()) {
			throw new StateException("Disaster map not visible");
		}
	}

	/*************************************************************************
	 * Checks that the twitter feed is visible with the most recent tweets.
	 * 
	 *************************************************************************/
	public void checkTwitterFeed() throws FeedException {
		Iterator<WebElementFacade> feedIter = twitterFeed.iterator();
		this.evaluateJavascript("window.scrollTo(0,document.body.scrollHeight)");
		System.out.println(twitterFeed.size() + "");
		if (twitterFeed.size() != 3) {
			throw new FeedException("Expected: <3>, actual value: <" + twitterFeed.size() + ">");
		}
		while (feedIter.hasNext()) {
			WebElementFacade feed = feedIter.next();
			if (!feed.isVisible()) {
				throw new FeedException("Twitter feed not visible");
			}
		}
	}

	/*************************************************************************
	 * Verifies that the FEMA twitter feed is visible on the homepage.
	 * 
	 *************************************************************************/
	public void checkTwitterFeedBlock() throws FeedException {
		if (!twitterFeedBlock.isVisible()) {
			throw new FeedException("Twitter feed not visible");
		}
	}

	/*************************************************************************
	 * Verifies that local resources results are visible and appear on the
	 * homepage.
	 * 
	 *************************************************************************/
	public void verifyLocalResourcesResults() throws LocalResourcesException {
		Iterator<WebElementFacade> localResourcesIter = localResourcesResults.iterator();
		while (localResourcesIter.hasNext()) {
			WebElementFacade resource = localResourcesIter.next();
			if (!resource.isVisible()) {
				throw new LocalResourcesException("Local resource not visible");
			}
		}
	}

	/*************************************************************************
	 * Returns the number of FOAs that match with the expected list.
	 * 
	 * @return Number of FOA titles that match with the given list
	 *************************************************************************/
	public int getNumAdditionalFOA() {
		String[] additionalFOATitles = {
				"203(h) Mortgage Insurance for Disaster Victims and 203(k) Rehabilitation Mortgage Insurance",
				"Disaster Recovery Center (DRC) / DRC Locator",
				"International Terrorism Victim Expense Reimbursement Program (ITVERP)",
				"Savings Bond Redemption and Replacement", "State Crime Victims Compensation",
				"Substance Abuse and Mental Health Services Administration (SAMHSA) Disaster Relief Information" };

		int foaCounter = 0;

		Iterator<WebElementFacade> iter = autoFOAs.iterator();
		while (iter.hasNext()) {
			String foaTitle = iter.next().getText();
			for (int i = 0; i < additionalFOATitles.length; i++) {
				if (additionalFOATitles[i].equals(foaTitle)) {
					foaCounter++;
					break;
				}
			}
		}

		return foaCounter;
	}

	/*************************************************************************
	 * Expands and collapses individual FOAs while also making sure the content
	 * is displayed in each.
	 * 
	 * @return The number of FOAs it clicked and checked for content
	 *************************************************************************/
	// TODO Finish this story check print and email buttons
	// TODO Clean this up somehow in the future
	//	Find a way around using multiple thread.sleeps?
	public int clickIndividualFOAs() {

		int successCounter = 0;
		Iterator<WebElementFacade> foaIter = FOAResults.iterator();
		while (foaIter.hasNext()) {
			WebElementFacade foa = foaIter.next();
			
			//TODO find a better and more succinct way to write this
			this.evaluateJavascript("window.scrollTo(0," + foa.getLocation().getY() + "); "
					+ "window.scrollBy(0, -(window.screen.availHeight / 2))");

			try {
				foa.click();
			} catch (Exception e) {
				this.evaluateJavascript("window.scrollTo(0,document.body.scrollHeight)"); // scroll to end of page
				foa.click();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			WebElement openContent = this.getDriver().findElement(
					By.xpath("//div[@class='foaccordionable open']/.." + "/div[@style[not(contains(., 'none'))]]"));

			if (openContent.isDisplayed()) {
				successCounter++;
			}

			foa.click();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return successCounter;
	}
	
	/*************************************************************************
	 * Returns true if you can see both of the buttons on the footer of
	 * the FOA questionnaire page. Currently it is only looking for the next
	 * and back buttons
	 * 
	 * @return true if the next and back buttons are visible
	 *************************************************************************/
	public boolean foaFooterIsVisible() {
		Iterator<WebElementFacade> iter = FOAfooter.iterator();
		int textMatchCounter = 0;
		
		while(iter.hasNext()) {
			WebElement button = iter.next().findElement(By.tagName("button"));
			if(button.isDisplayed()) {
				textMatchCounter++;
			}
		}
		
		return (textMatchCounter == 2);
	}
	
	/*************************************************************************
	 * Opens and closes each accordion on the final page of the
	 * questionnaire and returns the number of content sections it could see
	 * 
	 * @return number of content sections visible
	 *************************************************************************/
	public int numApplyOnlineFOAs() {
		Iterator<WebElementFacade> iter = FOAChecklistAccordions.iterator();
		int contentCounter = 0;
		
		while(iter.hasNext()) {
			WebElementFacade accordion = iter.next();
			
			try {
				accordion.click();
			} catch (Exception e) {
				this.evaluateJavascript("window.scrollTo(0,document.body.scrollHeight)"); // scroll to end of page
				accordion.click();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		}
		
		return contentCounter;
		
	}
	
	/*************************************************************************
	 * Verifies that Employment results and content are visible and appear under
	 * the accordions.
	 * 
	 * @throws EmploymentException
	 * 
	 *************************************************************************/
	public void verifyEmploymentVisibility() throws EmploymentException {
		Iterator<WebElementFacade> FOAResultsIter = FOAResults.iterator();
		Iterator<WebElementFacade> FOAExpandedResultsIter = FOAResults.iterator();
		while (FOAResultsIter.hasNext() && FOAExpandedResultsIter.hasNext()) {
			WebElementFacade result = FOAResultsIter.next();
			WebElementFacade expandedResult = FOAExpandedResultsIter.next();
			if (!result.isVisible() || !expandedResult.isVisible()) {
				throw new EmploymentException("Employment results not visible");
			}
		}
	}

	public int numberOfLandingPageNodes() {
		return landingPageNode.size();
	}

	public String pullPageTitle() {
		return pageTitle.getText();
	}

	public boolean addressLookupIsDisplayed() {
		return addressLookup.isDisplayed();
	}

	public boolean questionnaireIsDisplayed() {
		return questionnaire.isDisplayed();
	}

	public boolean textCaptchaIsDisplayed() {
		return textCaptcha.isDisplayed();
	}

	public boolean checkStatusPageIsDisplayed() {
		return checkStatusPageContent.isDisplayed();
	}

	public int getNumberAccordions() {
		return accordionBlocks.size();
	}

	public int getNumQuestionnaireResults() {
		return FOAResults.size();
	}

	public void clickEmploymentCheckbox() {
		getQuestionnaireButton("Employment").click();
	}

	public void getFOAResultsPage() {
		benefitCounter.click();
	}

	public int getNumEmploymentResults() {
		return FOAExpandedResults.size();
	}

	public void expandFOAResults() {
		expandAllButton.click();
	}

	public void collapseFOAResults() {
		collapseAllButton.click();
	}

	public int getNumExpandedQuestionnaireResults() {
		return FOAExpandedResults.size();
	}

	public void clickNextFOA() {
		nextButtonFOA.click();
	}

	public void clickBackFOA() {
		backButtonFOA.click();
	}

	public void lookUpLocation() {
		localResourcesTextbox.sendKeys("New York, NY");
	}
}
