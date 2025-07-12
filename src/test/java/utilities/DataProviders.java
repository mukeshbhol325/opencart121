package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

	// DataProvider 1
	@DataProvider(name="LoginData")
	public String[][] getData() throws IOException {
	    String path = ".\\testdata\\login_data.xlsx"; // taking xl file from testData

	    ExcelUtility xlutil = new ExcelUtility(path); // creating an object for XLUtility

	    int totalrows = xlutil.getRowCount("Sheet1");
	    int totalcols = xlutil.getCellCount("Sheet1", 1);

	    String logindata[][] = new String[totalrows][totalcols]; // created for two dimension array which can store all the data

	    for(int i = 1; i <= totalrows; i++) { // read the data from xl storing in two dimensional array
	        for(int j = 0; j < totalcols; j++) { // i is rows, j is columns
	            logindata[i-1][j] = xlutil.getCellData("Sheet1", i, j); // 1,0
	        }
	    }

	    return logindata; // returning two dimension array
	}

}
