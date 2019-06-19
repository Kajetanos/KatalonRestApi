package db

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import com.kms.katalon.core.logging.KeywordLogger as KeywordLogger
import java.sql.DriverManager as DriverManager
import java.sql.ResultSet as ResultSet
import java.sql.Statement as Statement
import com.mysql.jdbc.Connection as Connection
import internal.GlobalVariable as GlobalVariable

public class Connector {

	private static Connection connection = null;
	private static KeywordLogger log = new KeywordLogger()

	@Keyword
	def connectToDb(){
		Class.forName('org.h2.Driver')
		log.logInfo('Start try to connect')
		String conn = 'jdbc:h2:tcp://localhost:8083/mem:dbname'
		//		connection = DriverManager.getConnection('jdbc:h2:tcp://localhost:8083/mem:dbname', 'sa', 'password')
		java.sql.Connection c = DriverManager.getConnection('jdbc:h2:tcp://localhost:8083/mem:dbname', 'sa', 'password')
		//		log.logInfo('Get connection')
		//		Statement stm = connection.createStatement()
		return c
	}
	@Keyword
	def execute( String sql){
		Statement stm = connectToDb().createStatement()
		ResultSet rs = stm.executeQuery(sql)
		return rs
	}
}
