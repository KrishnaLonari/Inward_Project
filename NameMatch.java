package com.api.remitGuru.component.util;

//import org.apache.commons.codec.language.Soundex;

public class NameMatch
{
	/*public static boolean compareNames(String a, String b)
	{
		Soundex soundex = new Soundex();
		String phoneticValue = soundex.encode(a);
		String phoneticValue2 = soundex.encode(b);
		return phoneticValue.equals(phoneticValue2);
	}
	
	public static double similarity(String s1, String s2)
	{
		s1 = s1.replaceAll("[,\\s\\-\\'\\.\\(\\)\\?@!]", "");
		s2 = s2.replaceAll("[,\\s\\-\\'\\.\\(\\)\\?@!]", "");

		String longer = s1, shorter = s2;
		if (s1.length() < s2.length())
		{
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0)
		{
			return 1.0;
		}
		return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	}

	public static int editDistance(String s1, String s2)
	{
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++)
		{
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++)
			{
				if (i == 0)
					costs[j] = j;
				else
				{
					if (j > 0)
					{
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}

	public static int getMaxPercentMatch(String custName, String sanctionName)
	{
		int removingmiddlenamepercent = 0;
		int removingmiddlenamepercent2 = 0;
		int nameswappercent = 0;
		int nameswappercent2 = 0;
		int asispercent = 0;
		String match = null;
		String tempName2 = "";
		NameMatch nm = new NameMatch();

		if(sanctionName != null && !sanctionName.equals(""))
		{
			sanctionName = sanctionName.replaceAll(",", "").replaceAll("\\.", "").trim();

			String tempNameSplit[] = sanctionName.split(" ");
			String tempNameSplit2[] = custName.split(" ");
			String tempName = sanctionName;
			String tempNamecustname = custName;
			
			tempName2 = sanctionName;
			tempName = tempName.replaceAll("  ", " ");
			tempNamecustname = tempName2.replaceAll("  ", " ");
			asispercent = (int) (nm.similarity(custName, tempName) * 100);

			if (asispercent < 100)
			{
				if (tempNameSplit.length > 2)
				{
					tempName = (tempNameSplit[0] + tempNameSplit[2]).replaceAll("  ", " ");

					if (tempNameSplit.length > 3)
						tempName = (tempNameSplit[0] + tempNameSplit[2] + tempNameSplit[3]).replaceAll("  ", " ");

					removingmiddlenamepercent = (int) (nm.similarity(custName, tempName) * 100);
				}

				if (removingmiddlenamepercent < 100)
				{
					if (tempNameSplit2.length > 2)
					{
						tempNamecustname = (tempNameSplit2[0] + tempNameSplit2[2]).replaceAll("  ", " ");
						if (tempNameSplit2.length > 3)
							tempNamecustname = (tempNameSplit2[0] + tempNameSplit2[2] + tempNameSplit2[3]).replaceAll("  ", " ");

						removingmiddlenamepercent2 = (int) (nm.similarity(tempNamecustname, sanctionName) * 100);
						if (removingmiddlenamepercent2 > removingmiddlenamepercent)
							removingmiddlenamepercent = removingmiddlenamepercent2;
					}
				}

				if (tempNameSplit.length > 1)
				{
					if (tempNameSplit.length == 2)
						tempName = (tempNameSplit[1] + tempNameSplit[0]).replaceAll("  ", " ");
					else if (tempNameSplit.length == 3)
						tempName = (tempNameSplit[2] + tempNameSplit[1] + tempNameSplit[0]).replaceAll("  ", " ");
					else if (tempNameSplit.length > 3)
						tempName = (tempNameSplit[3] + tempNameSplit[1] + tempNameSplit[2] + tempNameSplit[0]).replaceAll("  ", " ");

					nameswappercent = (int) (nm.similarity(custName, tempName) * 100);
				}
				if (nameswappercent < 100)
				{
					if (tempNameSplit2.length > 1)
					{
						if (tempNameSplit2.length == 2)
							tempNamecustname = (tempNameSplit2[1] + tempNameSplit2[0]).replaceAll("  ", " ");
						else if (tempNameSplit2.length == 3)
							tempNamecustname = (tempNameSplit2[2] + tempNameSplit2[1] + tempNameSplit2[0]).replaceAll("  ", " ");
						else if (tempNameSplit2.length > 3)
							tempNamecustname = (tempNameSplit2[3] + tempNameSplit2[1] + tempNameSplit2[2] + tempNameSplit2[0]).replaceAll("  ", " ");

						nameswappercent2 = (int) (nm.similarity(tempNamecustname, sanctionName) * 100);
						if (nameswappercent2 > nameswappercent)
							nameswappercent = nameswappercent2;
					}
				}
			}
		}
		
		return Math.max(Math.max(Math.max(Math.max(asispercent, nameswappercent), nameswappercent2), removingmiddlenamepercent), removingmiddlenamepercent2); 
	}
	
	/*public static void printSimilarity(String s, String t)
	{
		System.out.println(String.format("%f %s is the similarity between \"%s\" and \"%s\"", (similarity(s, t) * 100),
				"%", s, t));
	}*/
	/*
	public static void main(String[] args) {
		System.out.println(similarity("ShubhamGuptaABC", "sdfsefsd"));
	}*/
}
