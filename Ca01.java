package CA01;

public class Ca01 {

	static int size = 50;
	static int dry = 0;
	static int wet = 1;
	static double energy_zero = 0;
	static double energy_max = 100;
	static double decrease = 0.62;
	static double stop = 53;
	static double top = 0.34;
	static double side = (1 - top)/2;

	public static void main(String[] args) {

		Water test [][] = new Water[size][size];
		for(int i = 0; i < size; i++) {
			if(i == size - 1)
				for(int j = 0; j < size; j++)
					test[i][j] = new Water(wet, energy_max);
			else
				for(int j = 0; j < size; j++)
					test[i][j] = new Water(dry, energy_zero);
		}

		print_test(test);

		for(int i = size - 1; i > 0; i--) {
			if(keep_going(test, i)) {
				action(test, i - 1);
				print_test(test);
			}
			else
				break;
		}

	}

	static void print_test(Water test[][]){ // print test[size][size]

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++)
				System.out.print(test[i][j].state);
			System.out.println();
		}
		System.out.println();

	}

	static void action(Water test[][], int place) {	// 파도가 오는 것

		for(int i = size - 1; i > place; i--) {
			for(int j = size - 1; j >= 0; j--) {
				if(j == size - 1) {
					test[i - 1][j].energy = test[i][j].energy * decrease * top;
					test[i - 1][j - 1].energy = test[i][j].energy * decrease * side;
				}
				else if(j == 0) {
					test[i - 1][j].energy += test[i][j].energy * decrease * top;
					test[i - 1][j + 1].energy += test[i][j].energy * decrease * side;
				}
				else {
					test[i - 1][j - 1].energy += test[i][j].energy * decrease * side;
					test[i - 1][j].energy += test[i][j].energy * decrease * top;
					test[i - 1][j + 1].energy += test[i][j].energy * decrease * side;
				}
				test[i][j].energy *= 1 - decrease;
			}
			judge(test, i - 1);

		}

	}

	static void judge(Water test[][],int place) {	// wet or dry 구분

		for(int i = 0; i < size; i++)
			if(test[place][i].energy >= stop)
				test[place][i].state = wet;

	}

	static boolean keep_going(Water test[][], int place) {	// 계속 가는지 멈췄는지 확인

		boolean decision = false;

		for(int i = 0; i < size; i++) {
			if(test[place][i].state == 1 && test[place][i].energy >= stop) {
				decision = true;
				break;
			}
		}

		return decision;
	}

}
