package CA;


// 123
// 4 5	방향 모두 건물인 경우 생각추가하기
// 현재 123방향 추가했고 나머지 케이스도 생각하기
// 45방향이 애매하게 됨 (수정) 표시 부분 수정


public class Ca02 {

	static int size = 50;	// test의 크
	static int soil = 0;	// 흙부분
	static int water = 1;	// 물부분
	static int tsunami = 2;	// 쓰나미부분
	static int building = 3;// 건물부분
	static int dry = 0;		// 물에 젖지 않은 부분을 표시
	static int wet = 1;		// 물에 젖은 부분을 표시
	static double energy_first = 0;	// 배열 설정 시 아직 에너지가 전달 되지 않은 부분을 표시 
	static double stop = 0.1;	// 이 크기보다 작은 에너지를 가진배열이 있을 경우 더이상 전진 불
	static double root = 1 + Math.sqrt(2);
	static double top = 1.0/root;	// 정면으로 얼마만큼의 크기가 이동하는지
	static double side = (1 - top)/2;	// 정면 외 2방향으로 얼마만큼의 크기가 이동하는지

	public static void main(String[] args) {

		Type_and_State test [][] = new Type_and_State[size][size];	// Type_and_State 클래스의 size만한 이차원 배열 생성

		make_array(test);
		print_test(test);	// 초기 설정된 배열의 상태를 출력

		for(int i = size - 1; i > 0; i--) {	// 배열의 크기를 고려해서 최대한 시뮬레이션 할 수 있도록 범위 설
			if(keep_going(test, i)) {	// keep_going 함수를 통해 계속 돌아갈수 있는지 확인
				action(test, i);	// 시물레이션
				print_test(test);		// action 을 통한 후 현재 배열의 상태를 출력
			}
			else						// 굳이 추가로 for 문을 돌지 않아도 됨으로 break
				break;
		}

	}

	static void make_array(Type_and_State test[][]) {

		for(int i = 0; i < size; i++) {
			if(i == size - 1)
				for(int j = 0; j < size; j++) {
					double energy = (double)(Math.random()*40)+80;	// 파도의 힘이 다 다르다고 해서 추가 최소 70 최대 100
					test[i][j] = new Type_and_State(tsunami, wet, energy);	// 객체 생성 후 test 이차원 배열에 입력
				}
			else if(i >= size - 10 && i <= size - 1) {
				for(int j = 0; j < size; j++)
					test[i][j] = new Type_and_State(water, wet, energy_first);
			}
			/*else
				for(int j = 0; j < size; j++)
					test[i][j] = new Type_and_State(soil, dry, energy_first);	// 파도가 없고 모두 에너지가 없다고 가정
			 */
			else	// 임의적으로 건물과 땅을 지음
				for(int j = 0; j < size; j++) {
					int rand = (int)(Math.random()*2);
					if(rand == 1)
						test[i][j] = new Type_and_State(soil, dry, energy_first);
					else
						test[i][j] = new Type_and_State(building, dry, energy_first);
				}
		}

	}

	static void print_test(Type_and_State test[][]){ // print test[size][size]

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++)
				if(test[i][j].type == soil)
					System.out.print("□ ");
				else if(test[i][j].type == water)
					System.out.print("▤ ");
				else if(test[i][j].type == tsunami)
					System.out.print("■ ");
				else if(test[i][j].type == building)
					System.out.print("^ ");

			System.out.println();
		}
		System.out.println();

	}

	static void action(Type_and_State test[][], int place) {	// 파도가 오는 것
		// place 는 현 위치 배열
		for(int i = size - 1; i >= 0; i--) {
			double decrease = (double)(Math.random()*0.1) + 0.75;	// 젖은 배열의 칸에서 젖지 않은 배열의 칸으로 이동시 앞이 building 이 아닌 경우 에너지 감쇄율 랜덤으로
			double decrease_building = (double)(Math.random()*0.1) + 0.75; // 젖은 배열의 칸에서 젖지 않은 배열의 칸으로 이동시 앞이 building 인 경우 에너지 감쇄율 랜덤으로
			if(i == size - 1 && test[place][i].energy >= stop && is_building(test, place - 1, i)) {	// 맨 오른쪽의 배열이고 배열의 에너지가 stop 	보다 크거나 같을 경우 + 앞칸의 셀이 건물이 아닐 경우
				if(is_building(test, place - 1, i - 1)) {	// 왼쪽 위 방향 셀이 건물이 아닌 경우 정상적으로 실행
					test[place - 1][i].energy += test[place][i].energy * decrease * top;
					test[place - 1][i - 1].energy += test[place][i].energy * decrease * side;
				}
				else if(!is_building(test, place - 1, i - 1) && is_building(test, place, i - 1))	// 아닌 경우 현재 위치 왼쪽으로 decrease 만큼 힘 이동 + 현재 위치 왼쪽 셀이 건물이 아닌 경우
					test[place][i - 1].energy += test[place][i].energy * decrease_building;

			}
			else if(i == 0 && test[place][i].energy >= stop && is_building(test, place - 1, i)) {		// 맨 왼쪽의 배열이고 배열의 에너지가 stop 보다 크거나 같을 경우 + 앞칸의 셀이 건물이 아닐 경우
				if(is_building(test, place - 1, i + 1)) {	// 오른쪽 위 방향 셀이 건물이 아닌 경우 정상적으로 실행
					test[place - 1][i].energy += test[place][i].energy * decrease * top;
					test[place - 1][i + 1].energy += test[place][i].energy * decrease * side;
				}
				else if(!is_building(test, place - 1, i + 1) && is_building(test, place, i + 1))	// 아닌 경우 현재 위치 오른쪽으로 decrease 만큼 힘 이동 + 현재 위치 오른쪽 셀이 건물이 아닌 경우
					test[place][i + 1].energy += test[place][i].energy * decrease_building;
			}
			else if(test[place][i].energy >= stop && is_building(test, place - 1, i)){				// 위의 경우를 제외한 보편적인 경우이고 배열의 에너지가 stop 보다 크거나 같을 경우 + 앞칸의 셀이 건물이 아닐 경우
				if(is_building(test, place - 1, i + 1) && is_building(test, place - 1, i - 1)) {	// 위쪽 양방향이 건물이 아닌 경우
					test[place - 1][i - 1].energy += test[place][i].energy * decrease * side;
					test[place - 1][i].energy += test[place][i].energy * decrease * top;
					test[place - 1][i + 1].energy += test[place][i].energy * decrease * side;
				}
				else if(!is_building(test, place - 1, i + 1) && is_building(test, place - 1, i - 1) && is_building(test, place, i + 1)) {	// 위쪽 오른방향이 건물이고 왼방향은 아닌 경우 + 현재 위치 오른쪽 셀이 건물이 아닌 경우
					test[place - 1][i - 1].energy += test[place][i].energy * decrease * side;
					test[place - 1][i].energy += test[place][i].energy * decrease * top;
					test[place][i + 1].energy += test[place][i].energy * decrease_building;
				}
				else if(is_building(test, place - 1, i + 1) && !is_building(test, place - 1, i - 1) && is_building(test, place, i - 1)) {	// 위쪽 왼방향이 건물이고 오른방향은 아닌 경우  + 현재 위치 왼쪽 셀이 건물이 아닌 경우
					test[place][i - 1].energy += test[place][i].energy * decrease_building;
					test[place - 1][i].energy += test[place][i].energy * decrease * top;
					test[place - 1][i + 1].energy += test[place][i].energy * decrease * side;
				}
				else if(!is_building(test, place - 1, i + 1) && !is_building(test, place - 1, i - 1)) {	// 위쪽 양방향이 건물인 경우(수정 필요)
					test[place][i - 1].energy += test[place][i].energy * decrease_building;
					test[place - 1][i].energy += test[place][i].energy * decrease * top;
					test[place][i + 1].energy += test[place][i].energy * decrease_building;
				}
			}
			else if(i == size - 1 && test[place][i].energy >= stop && !is_building(test, place, i)) {	// 맨 오른쪽의 배열이고 배열의 에너지가 stop 	보다 크거나 같을 경우 + 앞칸의 셀이 건물 일 경우
				if(is_building(test, place - 1, i - 1))	// 왼쪽 위 방향 셀이 건물이 아닌 경우 정상적으로 실행
					test[place - 1][i - 1].energy += test[place][i].energy * decrease * side;
				else	// 아닌 경우 현재 위치 왼쪽으로 decrease 만큼 힘 이동
					test[place][i - 1].energy += test[place][i].energy * decrease_building;

			}
			else if(i == 0 && test[place][i].energy >= stop && !is_building(test, place, i)) {		// 맨 왼쪽의 배열이고 배열의 에너지가 stop 보다 크거나 같을 경우 + 앞칸의 셀이 건물 일 경우
				if(is_building(test, place - 1, i + 1))	// 오른쪽 위 방향 셀이 건물이 아닌 경우 정상적으로 실행
					test[place - 1][i + 1].energy += test[place][i].energy * decrease * side;
				else	// 아닌 경우 현재 위치 오른쪽으로 decrease 만큼 힘 이동
					test[place][i + 1].energy += test[place][i].energy * decrease_building;
			}
			else if(test[place][i].energy >= stop && !is_building(test, place, i)){				// 위의 경우를 제외한 보편적인 경우이고 배열의 에너지가 stop 보다 크거나 같을 경우 + 앞칸의 셀이 건물 일 경우
				if(is_building(test, place - 1, i + 1) && is_building(test, place - 1, i - 1)) {	// 위쪽 양방향이 건물이 아닌 경우
					test[place - 1][i - 1].energy += test[place][i].energy * decrease * side;
					test[place - 1][i + 1].energy += test[place][i].energy * decrease * side;
				}
				else if(!is_building(test, place - 1, i + 1) && is_building(test, place - 1, i - 1) && is_building(test, place, i + 1)) {	// 위쪽 오른방향이 건물이고 왼방향은 아닌 경우 + 현재 위치 오른쪽 셀이 건물이 아닌 경우
					test[place - 1][i - 1].energy += test[place][i].energy * decrease * side;
					test[place][i + 1].energy += test[place][i].energy * decrease_building;
				}
				else if(is_building(test, place - 1, i + 1) && !is_building(test, place - 1, i - 1) && is_building(test, place, i - 1)) {	// 위쪽 왼방향이 건물이고 오른방향은 아닌 경우 + 현재 위치 왼쪽 셀이 건물이 아닌 경우
					test[place][i - 1].energy += test[place][i].energy * decrease_building;
					test[place - 1][i + 1].energy += test[place][i].energy * decrease * side;
				}
				else if(!is_building(test, place - 1, i + 1) && !is_building(test, place - 1, i - 1)) {	// 위쪽 양방향이 건물인 경우(수정 필요)
					test[place][i - 1].energy += test[place][i].energy * decrease_building;
					test[place][i + 1].energy += test[place][i].energy * decrease_building;
				}
			}
			// 배열에 앞으로 나갈 에너지가 부족한 경우는 필요 없으므로 else 를 제외 시
		}

		judge(test, place - 1);	// 배열을 보고 판단하여 wet 부분과 dry 부분을 최신화


	}

	static boolean is_building(Type_and_State test[][], int place, int k) {	// 셀이 건물인지 아닌지

		if(test[place][k].type == building)
			return false;
		
		return true;
	}

	static void judge(Type_and_State test[][], int place) {	// wet or dry 구분

		for(int i = 0; i < size; i++)
			if(test[place][i].energy >= stop && test[place][i].type != building)	// energy >= stop and array[place][i].type != building
				test[place][i].type = tsunami;

	}

	static boolean keep_going(Type_and_State test[][], int place) {	// 계속 가는지 멈췄는지 확인

		boolean decision = false;	// 초기 결정 설정

		for(int i = 0; i < size; i++) {
			if(test[place][i].type == tsunami && test[place][i].energy >= stop) {	// 제일 윗줄에 wet 이고 wet 인 배열의 에너지가 stop 보다 크거나 같은 배열이 하나라도 있으면 true 로 결정 후 break
				decision = true;
				break;
			}
		}

		return decision;		// 결정으로 return
	}

}
