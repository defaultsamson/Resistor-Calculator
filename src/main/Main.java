package main;

public class Main
{
	public static final float MAX_RESISTANCE = 20.0F; // The maximum resistor value in kOhm
	public static final float RESULT_ACCURACY = 0.1F; // Accuracy in kOhm
	public static final float INPUT_VOLTAGE = 5.0F; // The input voltage
	
	public static void main(String[] args)
	{
		float[] result = formatFloats(findBestResistorCombo(MAX_RESISTANCE, RESULT_ACCURACY, INPUT_VOLTAGE));
		
		System.out.println();
		System.out.println("     out              V = " + INPUT_VOLTAGE + " V");
		System.out.println("      |              R1 = " + result[0] + " kOhms");
		System.out.println("      |-R3-S1-Gnd    R2 = " + result[1] + " kOhms");
		System.out.println(" V-R1-|              R3 = " + result[2] + " kOhms");
		System.out.println("      |-R2-S2-Gnd    LD = " + result[3] + " kOhms");
		System.out.println();
		System.out.println(" S1 | S2 | out");
		System.out.println(" 0  | 0  | " + result[4] + " V");
		System.out.println(" 1  | 0  | " + result[5] + " V");
		System.out.println(" 0  | 1  | " + result[6] + " V");
		System.out.println(" 1  | 1  | " + result[7] + " V");
		
		System.out.println();
	}
	
	public static float[] formatFloats(float ... values)
	{
		float[] toReturn = new float[values.length];
		
		for (int i = 0; i < values.length; i++)
		{
			// Makes display values show to the nearest hundredth
			float value = (float) Math.round(values[i] * 100) / 100;
			
			toReturn[i] = value;
		}
		
		return toReturn;
	}
	
	public static float[] findBestResistorCombo(float maxRes, float step, float voltage)
	{
		float[] resistor = {0F, 0F, 0F};
		
		float[] bestValues = {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F};
		float minimumValue = 0F; // Higher is better
		
		// Will test all the values to the maximum resistance
		while(resistor[0] < maxRes)
		{
			resistor[1] = 0F;
			while(resistor[1] < maxRes)
			{
				resistor[2] = 0F;
				while(resistor[2] < maxRes)
				{
					//System.out.println("Working on it... (" + (((resistor[0] + ((resistor[1] + (resistor[2] / (maxRes / step))) / (maxRes / step))) / (maxRes / step)) * 100F) + "%)");
					
					float[] voltageOutputTable = {
							voltage, 
							(resistor[1] / (resistor[1] + resistor[0])) * voltage, 
							(resistor[2] / (resistor[2] + resistor[0])) * voltage, 
							(resistor[0] / (resistor[0] + (1 / ((1 / resistor[1]) + (1 / resistor[2]))))) * voltage};
					
					float minimumDif = minimumvalue(voltageOutputTable);
					
					if (minimumDif > minimumValue)
					{
						minimumValue = minimumDif;
						
						bestValues[0] = resistor[0];
						bestValues[1] = resistor[1];
						bestValues[2] = resistor[2];
						
						bestValues[3] = minimumValue;
						
						bestValues[4] = voltageOutputTable[0];
						bestValues[5] = voltageOutputTable[1];
						bestValues[6] = voltageOutputTable[2];
						bestValues[7] = voltageOutputTable[3];
					}
					
					resistor[2]+=step;
				}
				resistor[1]+=step;
			}
			resistor[0]+=step;
		}
		
		return bestValues;
	}
	
	/**
	 * Finds the lowest difference between two numbers out of multiple numbers.
	 * <p>
	 * Higher is better!
	 * 
	 * @param value  the numbers
	 * @return the lowest difference between any of the two numbers in <b>value</b>
	 */
	public static float minimumvalue(float ... value)
	{
		// Calculates the current minimum
		float currentMin = 0F;
		for (int i = 0; i < value.length; i++)
		{
			currentMin += value[i];
		}
		
		for (int i = 0; i < value.length; i++)
		{
			for (int i2 = 0; i2 < value.length; i2++)
			{
				// If it's not testing against itself
				if (i != i2)
				{
					float prev = value[i];
					float current = value[i2];
					float difference = 0F;
					
					if (prev < current)
					{
						difference = current - prev;
					}
					else if (prev > current)
					{
						difference = prev - current;
					}
					
					if (difference < currentMin)
					{
						currentMin = difference;
					}
				}
			}
		}
		
		return currentMin;
	}
}
