/**
 * @author Han Zhang
 * hanzhan2
 */
package hw3;

public class Female extends Person {
    float[][] nutriConstantsTableFemale = new float[][]{
        //AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE 
        {1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.71f, 0.8f, 0.8f, 0.8f}, //0: Protein constants
        {60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //1: Carbohydrate
        {19, 19, 19, 19, 25, 26, 26, 25, 25, 21},  //2: Fiber constants
        {36, 36, 32, 21, 16, 15, 14, 14, 14, 14}, 	//3: Histidine
        {88, 88, 43, 28, 22, 21, 19, 19, 19, 19}, 	//4: isoleucine
        {156, 156, 93, 63, 49, 47, 44 , 42, 42, 42},//5: leucine
        {107, 107, 89, 58, 46, 43, 40, 38, 38, 38}, //6: lysine
        {59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//7: methionine
        {59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//8: cysteine
        {135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //9: phenylalanine
        {135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //10: phenylalanine
        {73, 73, 49, 32, 24, 22, 21, 20, 20, 20}, 	//11: threonine
        {28, 28, 13, 8, 6, 6, 5, 5, 5, 5}, 			//12: tryptophan
        {87, 87, 58, 37, 28, 27, 24, 24, 24, 24	}  	//13: valine
    };
    //non-default constructor to set values and initialize nutro constants table
    Female(float age, float weight, float height, float physicalActivityLevel, String ingredientsToAvoid) {
        super(age, weight , height, physicalActivityLevel, ingredientsToAvoid);
        initializeNutriConstantsTable();
    }

    //calculate energy requirement
    @Override
    float calculateEnergyRequirement() {
        float energyRequirement = 0;
        int index = ageGroup.getAgeGroupIndex();
        switch(index) {
        case(0): energyRequirement = 89 * weight + 75; break;
        case(1): energyRequirement = 89 * weight - 44; break;
        case(2): energyRequirement = 89 * weight - 78; break;
        case(3): energyRequirement = 89 * weight - 80; break;
        case(4):
        case(5):
        case(6): energyRequirement = (float) (135.3 - (30.8 * age) + physicalActivityLevel * ( 10 * weight + 934 * height / 100) + 20); break;
        case(7):
        case(8):
        case(9): energyRequirement = (float) (354 - (6.91 * age) + physicalActivityLevel * (9.36 * weight + 726 * height / 100)); break;
        }
        return energyRequirement;
    }
    
    //return the fixed table
    @Override
    void initializeNutriConstantsTable() {
        this.nutriConstantsTable = nutriConstantsTableFemale;
    }
}
