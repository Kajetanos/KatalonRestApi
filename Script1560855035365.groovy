import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.RequestObject as RequestObject
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webservice.verification.WSResponseManager as WSResponseManager
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import java.sql.DriverManager as DriverManager
import java.sql.ResultSet as ResultSet
import java.sql.Statement as Statement
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.mysql.jdbc.Connection as Connection
import java.util.HashMap
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.testobject.ResponseObject
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import db.Connector as Connector


import org.junit.After

KeywordLogger log = new KeywordLogger()
Class.forName('org.h2.Driver')
String token = "";

//def signUp = WS.sendRequest(findTestObject('signUp'))
//log.logInfo("Sending request to signup User. Status of request is: "+signUp.getStatusCode())
//if(signUp.getStatusCode().toString().equals("200")){
	def getToken = WS.sendRequest(findTestObject('getToken'))
	token = getToken.getHeaderFields().get("Authorization").toString().replace("[", "")
	log.logInfo("Status request to getting token is: "+getToken.getStatusCode().toString())
//}
	log.logInfo("Creating request with add token")
	def request = (RequestObject)findTestObject('GetAllCandy')

	ArrayList<TestObjectProperty> HTTPHeader = new ArrayList<TestObjectProperty>()
	HTTPHeader.add(new TestObjectProperty('Authorization', ConditionType.EQUALS, token.replace("]", "") ))
	HTTPHeader.add(new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json'))
	log.logInfo("Adding authorization token to header of request")
	request.setHttpHeaderProperties(HTTPHeader)
	log.logInfo("Sending request to endpoint 'http://localhost:8089/candy/all'") 
	ResponseObject respObj = WS.sendRequest(request)
	WS.verifyResponseStatusCode(respObj,200)
	log.logInfo("Request to get data is sended. Response code is: "+respObj.getStatusCode())
	log.logInfo("Parse JSON text to list")
	def candyList = new JsonSlurper().parseText(respObj.getResponseText())
	ResultSet rs = CustomKeywords.'db.Connector.execute'('SELECT * FROM candy')
	log.logInfo("Execute query 'SELECT * FROM candy' and create list with all fields")
	def list = []
	while (rs.next()) {
		list.add("["+"id:"+rs.getString('id')+", name:"+rs.getString('name')+ ", type:"+rs.getString('type')+"]" )
	}
	println(list.toString().equals(candyList.toString()))
	log.logInfo("Compare list from request and from DB")
	assert list.toString() == candyList.toString()
	WS.sendRequest(findTestObject('getCandy'))



