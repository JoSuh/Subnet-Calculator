import java.util.Scanner;


/** File name:	SubnetCalculator.java
 * Author:		Jo Suh
 * Date:		April. 14, 2019
 * Purpose:		This file holds the SubnetCalculator class.
 */



/** 
 * Calculates the subnet address and information
 * <p>
 * given a hostIP address, subnet, and number of subnets inputs,
 * 
 * @author Jo Suh
 * @version 1.0
 * @see java.io
 * @see java.lang
 * @since 1.0
 */

public class SubnetCalculator {
	/**
	 * the number of subnets to be displayed, set by the user
	 */
	private static int numberOfSubnetsToDisplay = 0;
	/**
	 * the number of bits borrowed (s)
	 */
	private static int bitsBorrowed = 0;
	/**
	 * the number of subnets (S)
	 */
	private static int numOfSubnets = 0;
	/**
	 * the number of host bits (h)
	 */
	private static int hostBits = 0;
	/**
	 * number of available Hosts(H)
	 */
	private static int numOfHosts = 0;
	/**
	 * the subnet index
	 */
	private static int subnetIndex = 0;
	/**
	 * the subnet mask input from the user in the slash form
	 */
	private static int subnetMask = 0;
	/**
	 * the number of hosts the address has
	 */
	private static int numberOfHosts =0;
	/**
	 * the number for indicating the (nth octet of the) start of the wildcard mask
	 */
	private static int wildcardOctet = 0;
	/**
	 * the class of the IP address
	 */
	private static String IPClass = "";

	
	/**
	 * the wildcard mask in decimal
	 */
	private static Integer[] wildCardD = new Integer[4];
	/**
	 * the Host IP address in binary
	 */
	private static String[] IPAddressB = new String[4];
	/**
	 * the Host IP address in decimal
	 */
	private static Integer[] IPAddressD= new Integer[4];
	/**
	 * the default subnet mask in binary
	 */
	private static String[] defaultMaskB = new String[4];
	/**
	 * the default subnet mask in decimal
	 */
	private static Integer[] defaultMaskD= new Integer[4];
	/**
	 * the subnetMask in binary
	 */
	private static String[] subnetMaskB = new String[4];
	/**
	 * the subnetMask in decimal
	 */
	private static Integer[] subnetMaskD= new Integer[4];
	/**
	 * the Network address in binary
	 */
	private static String[] networkB = new String[4];
	/**
	 * the Network address in decimal
	 */
	private static Integer[] networkD= new Integer[4];
	/**
	 * the Last host address in decimal
	 */
	private static Integer[] FHIPD= new Integer[4];
	/**
	 * the Last host address in binary
	 */
	private static String[] FHIPB= new String[4];
	/**
	 * the Last host address in decimal
	 */
	private static Integer[] LHIPD= new Integer[4];
	/**
	 * the Last host address in binary
	 */
	private static String[] LHIPB= new String[4];
	/**
	 * the Broadcast address in binary
	 */
	private static String[] BAB = new String[4];
	/**
	 * the Broadcast address in decimal
	 */
	private static Integer[] BAD= new Integer[4];
	
	
	/**
	 * converts an array of decimals to binary
	 * 
	 * @param input	an array of Integers holding decimal values
	 * @return an array of Strings holding binary values
	 */
	private static String[] decimalToBinary(Integer[] input) {
		//takes an string of address, converts to binary

		String[] binaryVal= new String[4];
		
		//for each address
		for (int i= 0; i<4; i++) {
			//for each octet
			String binVal = Integer.toBinaryString(input[i]);
			while (binVal.length() <8) {
				binVal= "0" + binVal;
			}
			binaryVal[i] = binVal;
		}
		return binaryVal;
	}
	/**
	 * prints the addresses with the dots
	 * 
	 * @param input	an array of Objects to print
	 */
	private static void printAddress(Object[] input) {
		//prints
		System.out.print(input[0] + "." + input[1] + "."  + input[2] + "."  + input[3]);
	}
	/**
	 * ANDs a subnetMask and Host IP address together
	 * 
	 * @param subnet an array of Strings holding the subnet mask value
	 * @param HostIP an array of Strings holding the host IP address
	 * @return an array of Strings holding the ANDed value
	 */
	private static String[] AND(String[] subnet, String[] HostIP) {
		
		String[] ANDval = new String[4];
		
		//for each octet
		for (int i= 0; i<4; i++) {
			String subnetBinary= "";
			
			//for each binary
			for (int each=0; each<8; each++) {
				char subnetVal = subnet[i].charAt(each);
				char IPVal = HostIP[i].charAt(each);
				
				if ( subnetVal == '1' ) {
					//if subnet mask is 1, the AND value depends on the host IP
					subnetBinary += IPVal;
				}else {
					//if subnet mask is 0, the AND value is always 0
					subnetBinary += "0";
				}
			}
			ANDval[i] = subnetBinary;
		}
		return ANDval;
	}
	/**
	 * converts from binary to decimal
	 * 
	 * @param binaryInput an array of Strings holding the binary value
	 * @return an array of Strings holding decimal values
	 */
	private static Integer[] binaryToDecimal(String[] binaryInput) {
		//separate the binaries into 8-digits
		Integer[] formatted = new Integer[4];
		//get the decimal values
		for (int i=0; i<4; i++) {
			formatted[i] = Integer.parseInt(binaryInput[i], 2); 
		}
		return formatted;
	}
	/**
	 * checks the range of an array (address) and modifies it to be correct
	 * 
	 * @param inputDecimal an array of Integers that hold decimal values
	 * @return an array of Integers holding formatted decimal values
	 */
	private static Integer[] checkRange(Integer[] inputDecimal) {
		//check for range 0-255 for each section
		Integer[] decimal = inputDecimal;
		
		for (int each=0; each<4; each++) {
			if ( decimal[3-each]>=256 ) {
				//inputDecimal[each-1]+= inputDecimal[each] - 255;
				//inputDecimal[each]= 255;
				decimal[3-each-1]+= 1;
				decimal[3-each]-= 256;
			}
			if ( decimal[3-each]<0 ) {
				decimal[3-each-1]-= 1;
				decimal[3-each]+= 256;
			}
		}

		return decimal;
	}
	/**
	 * adds two decimal values together
	 * 
	 * @param bin1Decimal an array of Integers holding the first decimal values
	 * @param bin2Decimal an array of Integers holding the second decimal values
	 * @param subtract a boolean value for indicating whether the operation should be a subtraction or not
	 * @return an array of Integers holding the added decimal values
	 */
	private static Integer[] addDecimal(Integer[] decimal1, Integer[] decimal2, boolean subtract) {

		decimal1 = checkRange(decimal1);
		decimal2 = checkRange(decimal2);
		
		Integer[] resultDecimal = new Integer[4];
		
		for (int i=0; i<4; i++) {
			if (!subtract) {
				resultDecimal[i] = decimal1[i] + decimal2[i];
			}else {
				resultDecimal[i] = decimal1[i] - decimal2[i];
			}
		}
		return checkRange(resultDecimal);
		
	}
	/**
	 * adds two binary values together
	 * 
	 * @param bin1 an array of Strings holding binary values
	 * @param bin2 a String holding a binary value
	 * @param subtract a boolean value for indicating whether the operation should be a subtraction or not
	 * @return an array of Integers holding the added decimal values
	 */
	private static Integer[] addBinary(String[] bin1, String bin2, boolean subtract) {
		return addDecimal( binaryToDecimal(bin1), binaryToDecimal(binaryStringToArray(bin2)), subtract);
	}
	/**
	 * converts Strings to arrays
	 * 
	 * @param inputVal the binary String
	 * @return an array of Strings holding binary values
	 */
	private static String[] binaryStringToArray(String inputVal) {
		String[] stringArray= new String[4];
		String value = inputVal;
		while (value.length() <32) {
			value = "0" + value;
		}
		for (int oct=0; oct<4; oct++) {
			stringArray[oct]= value.substring(0, 8);//convert the string values to number
			value = value.substring(8);
		}
		return stringArray;
	}
	/**
	 * Determines if a number is in the range of the indicated values
	 * 
	 * @param value an integer value to be checked
	 * @param min an integer value for the minimum range
	 * @param max an integer value for the maximum range
	 * @return a boolean value determining if the value is in the range or not
	 */
	private static boolean isBetween(int value, int min, int max) {
		  return (min <= value && value <= max);
	}
	/**
	 * Determines if each section of an address is in the correct range
	 * 
	 * @param address an array of Integers holding decimal values
	 * @return a boolean value determining if the value is in the range or not
	 */
	private static boolean isInRange(Integer[] address) {
		for (int each : address) {
			if (each < 0 || each > 255) {
				return false;
			}
		}
		return true;
	}
	/**
	 * converts number to that number of 1s
	 * @param num the number of 1s
	 * @return the String array of 1s,with 0s filling the range
	 */
	private static String[] numberOf1sToBinary(int num) {
		String subnetMaskInBinary = "";
		for (int i=0; i<32; i++) {
			if ( num > i) {
				subnetMaskInBinary += "1";
			}else {
				subnetMaskInBinary += "0";
			}
		}
		return binaryStringToArray(subnetMaskInBinary);
	}
	/**
	 * gets the host ranges and broadcast address given a subnet address
	 * @param SID an integer array of the subnet address
	 */
	private static void getAddresses(Integer[] SID) {

		String[] SIDB = decimalToBinary(SID);
		//FHIP==============================
		FHIPD = addBinary(SIDB, "1", false);
		FHIPB = decimalToBinary(FHIPD);

		//BA==============================
		String toAdd="";
		for(int i=0; i<hostBits; i++) {
			toAdd+="1";
		}
		BAD = addBinary(SIDB, toAdd, false);
		BAB = decimalToBinary(BAD);
		//LHIP==============================
		LHIPD = addBinary(BAB, "1", true); //subtract
		LHIPB = decimalToBinary(LHIPD);
	}
  
  
  
	/**
	 * main
	 * 
	 * @param args an array of Strings for the console
	 */
	public static void main (String[] args){
		Scanner input = new Scanner(System.in);
		
		try {
			//get user input----------------------
			System.out.println("Enter the Host IP Address and subnet mask(X.X.X.X /Y)");
			String inputAddress= input.nextLine().replaceAll("\\s+",""); //remove all spaces
			String[] divide= inputAddress.split("/");//separate the subnet mask portion
			String[] temp = divide[0].split("\\.");//to split by a dot, need to use \\
			
			//actually put into address array
			Integer[] IPAddressDecimal = new Integer[4];
			for (int oct=0; oct<4; oct++) {
				IPAddressD[oct]= Integer.valueOf(temp[oct]);//convert the string values to number
			}
			IPAddressB = decimalToBinary(IPAddressD);
			
			//get subnet mask
			subnetMask = Integer.valueOf(divide[1]);
			subnetMaskB = numberOf1sToBinary(subnetMask);
			subnetMaskD = binaryToDecimal(subnetMaskB);

			//check if valid
			if ( subnetMask>32 || !isInRange(IPAddressD) || !isInRange(subnetMaskD) ) {
				throw new Exception("Invalid input");
			}
			
			//numberOfSubnetsToDisplay==============================
			System.out.println("How many subnets would you like to display?");
			numberOfSubnetsToDisplay= input.nextInt();
			
			if (numberOfSubnetsToDisplay<0){
				throw new Exception("Invalid input");
			}
			
			//Class==============================
			int firstOctet = IPAddressD[0];
			int classSubnet;
			if (isBetween(firstOctet, 1, 126)){
				IPClass = "A";
				classSubnet = 8;
			}else if (isBetween(firstOctet, 128, 191)){
				IPClass = "B";
				classSubnet = 16;
			}else if (isBetween(firstOctet, 192, 223)){
				IPClass = "C";
				classSubnet = 24;
			}else if (isBetween(firstOctet, 224, 239)){
				IPClass = "D";
				classSubnet = 32;
			}else {
				throw new Exception("Invalid IP address");
			}
			
			if (classSubnet>subnetMask) {
				throw new Exception();
			}
			
			defaultMaskB = numberOf1sToBinary(classSubnet);
			defaultMaskD = binaryToDecimal(defaultMaskB);
			
			//Subnet address==============================
			networkB = AND(subnetMaskB, IPAddressB);
			networkD = binaryToDecimal(networkB);
					
			numberOfHosts = (int) Math.pow(2, 32-subnetMask) - 2;
			
			
			//Extras==============================
			bitsBorrowed = subnetMask - classSubnet;
			numOfSubnets = (int) Math.pow( 2, bitsBorrowed );

			hostBits = 32-subnetMask;
			numOfHosts = (int) Math.pow( 2, hostBits ) - 2;

			String networkString = networkB[0] + networkB[1] + networkB[2] + networkB[3];
			String subnetIndexB = networkString.substring(classSubnet, classSubnet+bitsBorrowed);
			subnetIndex = Integer.parseInt(subnetIndexB, 2);
			
			//wildcard==============================
			String wildCardValue = "";
			for (int i=0; i<32; i++) {
				if (i == subnetMask-1) {
					wildCardValue += "1";
				}else {
					wildCardValue += "0";
				}
			}
			String[] wildCardB = binaryStringToArray(wildCardValue);
			wildCardD = binaryToDecimal(wildCardB);
			wildcardOctet = subnetMask/8;
			
			
			System.out.println("------------------------------------------------------------------------------------------");

			//--------------------------
			System.out.print("\nIP class:\t\t\t" + IPClass);
			//--------------------------
			System.out.print("\nSubnet index:\t\t\t" + subnetIndex);
			//--------------------------
			System.out.print("\nBits borrowed\t\t(s):\t" + bitsBorrowed);
			//--------------------------
			System.out.print("\nNumber of subnets\t(S):\t" + numOfSubnets);
			//--------------------------
			System.out.print("\nNumber of host bits\t(h):\t" + hostBits );
			//--------------------------
			System.out.print("\nNumber of usable hosts\t(H):\t" + numOfHosts);
			//--------------------------
			System.out.print("\nTotal number of hosts:\t\t" + (numberOfHosts+2) );

			//--------------------------
			System.out.println("\n--------------------------------------------------------------------------");
			//--------------------------
			System.out.print("\nIP address:\t\t");
			printAddress(IPAddressD);
			System.out.print("\t\t");
			printAddress(IPAddressB);
			//--------------------------
			System.out.print("\nSubnet mask:\t\t");
			printAddress(subnetMaskD);
			System.out.print("\t\t");
			printAddress(subnetMaskB);
			//--------------------------
			System.out.print("\nDefault subnet mask:\t");
			printAddress(defaultMaskD);
			System.out.print("\t\t");
			printAddress(defaultMaskB);
			//--------------------------
			System.out.println("\n--------------------------------------------------------------------------");
			//--------------------------
			System.out.print("\nNetwork address:\t");
			printAddress(networkD);
			System.out.print("/" + subnetMask + "\t\t");
			printAddress(networkB);
			System.out.print("/" + subnetMask);
			//--------------------------
			System.out.print("\nSubnet ID:\t\t" + networkD[2]);
			//First 16 bits	Network ID
			//Next 8 bits	Subnet ID
			//Next 8 bits	Host ID
			//--------------------------
			System.out.println();
			getAddresses(networkD);
			//--------------------------
			System.out.print("\nFHIP:\t\t\t");
			printAddress(FHIPD);
			System.out.print("\t\t");
			printAddress(FHIPB);
			//--------------------------
			System.out.print("\nLHIP:\t\t\t");
			printAddress(LHIPD);
			System.out.print("\t\t");
			printAddress(LHIPB);
			//--------------------------
			System.out.print("\nHost Range:\t\t");
			printAddress(FHIPD);
			System.out.print(" - ");
			printAddress(LHIPD);
			//--------------------------
			System.out.print("\nBroadcast Address:\t");
			printAddress(BAD);

			//--------------------------
			System.out.println("\n--------------------------------------------------------------------------");
			//--------------------------
			
			String lessThanAsked = "the first ";
			if (numberOfSubnetsToDisplay > numOfSubnets) {
				numberOfSubnetsToDisplay = numOfSubnets;
				lessThanAsked = "a maximum number of ";
			}
			
			System.out.println("Displaying " + lessThanAsked + numberOfSubnetsToDisplay + " available subnets\n");
			System.out.println("\tSubnet\t\t\tFHIP\t\t\tLHIP\t\t\tBA");

			Integer[] aSubnet = networkD;
			for(int x=0; x<4; x++) {
				if (x>=wildcardOctet) {
					aSubnet[x] = 0;
				}
			}
			
			for (int i=0; i<numberOfSubnetsToDisplay; i++) {
				//calculate the addresses
				getAddresses(aSubnet);
				
				//print each address
				System.out.print( "\n" + (i+1) + "\t");
				printAddress(aSubnet);
				System.out.print("\t\t");
				printAddress(FHIPD);
				System.out.print("\t\t");
				printAddress(LHIPD);
				System.out.print("\t\t");
				printAddress(BAD);
				
				//calculate for the next subnet
				aSubnet = addDecimal(aSubnet, wildCardD, false);
				
			}
			System.out.println("\n\n");
			
		} catch (Exception x) {
			System.err.println("Invalid input");
			//System.err.println(x);
		}
	}
}
