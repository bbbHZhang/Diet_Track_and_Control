/**
 * @author Han Zhang
 * hanzhan2
 */
package hw2;


public abstract class Person {

    float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
    String ingredientsToWatch;
    float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

    NutriProfiler.AgeGroupEnum ageGroup;

    abstract void initializeNutriConstantsTable();
    abstract float calculateEnergyRequirement();

    Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
        //set values up
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.physicalActivityLevel = physicalActivityLevel;
        this.ingredientsToWatch = ingredientsToWatch;
        
        //match ageGroup to specific AgeGroupEnum with age
        if (age <= 0) {} //ignore negative age
        for(NutriProfiler.AgeGroupEnum e : NutriProfiler.AgeGroupEnum.values()) {
            if (age < e.getAge()) { 
                ageGroup = e;
                break;
            }
            else { continue; }
        }
    }

    //returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
    //Each value is calculated as follows:
    //For Protein, it multiples the constant with the person's weight.
    //For Carb and Fiber, it simply takes the constant from the 
    //nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
    //For others, it multiples the constant with the person's weight and divides by 1000.
    //Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc. 
    float[] calculateNutriRequirement() {
        int index = ageGroup.getAgeGroupIndex();
        float[] result =  new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];
        for (int i = 0; i <NutriProfiler.RECOMMENDED_NUTRI_COUNT; i++) {
            if (i == 0) { result[i] = nutriConstantsTable[i][index] * weight;}
            else if (i == 1 || i == 2) { result[i] = nutriConstantsTable[i][index]; }
            else { result[i] = nutriConstantsTable[i][index] * weight / 1000; }
        }
        return result;
    }
}
