package CA01;

public class Ca01 {
    
    static int size = 50;    // test의 크
    static int dry = 0;        // 물에 젖지 않은 부분을 표시
    static int wet = 1;        // 물에 젖은 부분을 표시
    static double energy_zero = 0;    // 배열 설정 시 아직 에너지가 전달 되지 않은 부분을 표시
    static double energy;        // 초기 파도의 에너지를 표시
    static double decrease = 0.70;    // 젖은 배열의 칸에서 젖지 않은 배열의 칸으로 이동시 에너지 감쇄율
    static double stop = 25;    // 이 크기보다 작은 에너지를 가진배열이 있을 경우 더이상 전진 불
    static double top = 0.34;    // 정면으로 얼마만큼의 크기가 이동하는지
    static double side = (1 - top)/2;    // 정면 외 2방향으로 얼마만큼의 크기가 이동하는지
    
    public static void main(String[] args) {
        
        Water test [][] = new Water[size][size];    // Water 클래스의 size만한 이차원 배열 생성
        for(int i = 0; i < size; i++) {
            if(i == size - 1)
                for(int j = 0; j < size; j++) {
                    energy = (double)Math.random()*100+70;    // 파도의 힘이 다 다르다고 해서 추가
                    test[i][j] = new Water(wet, energy);    // 객체 생성 후 test 이차원 배열에 입력
                }
            else
                for(int j = 0; j < size; j++)
                    test[i][j] = new Water(dry, energy_zero);    // 파도가 없고 모두 에너지가 없다고 가정
        }
        
        print_test(test);    // 초기 설정된 배열의 상태를 출력
        
        for(int i = size - 1; i > 0; i--) {    // 배열의 크기를 고려해서 최대한 시뮬레이션 할 수 있도록 범위 설
            if(keep_going(test, i)) {    // keep_going 함수를 통해 계속 돌아갈수 있는지 확인
                action(test, i - 1);    // 시물레이션
                print_test(test);        // action 을 통한 후 현재 배열의 상태를 출력
            }
            else                        // 굳이 추가로 for 문을 돌지 않아도 됨으로 break
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
    
    static void action(Water test[][], int place) {    // 파도가 오는 것
        
        for(int i = size - 1; i > place; i--) {
            for(int j = size - 1; j >= 0; j--) {
                if(j == size - 1 && test[i][j].energy >= stop) {    // 맨 오른쪽의 배열이고 배열의 에너지가 stop     보다 크거나 같을 경우
                    test[i - 1][j].energy += test[i][j].energy * decrease * top;
                    test[i - 1][j - 1].energy += test[i][j].energy * decrease * side;
                    test[i][j].energy *= 1 - decrease;
                }
                else if(j == 0 && test[i][j].energy >= stop) {        // 맨 쪽의 배열이고 배열의 에너지가 stop 보다 크거나 같을 경우
                    test[i - 1][j].energy += test[i][j].energy * decrease * top;
                    test[i - 1][j + 1].energy += test[i][j].energy * decrease * side;
                    test[i][j].energy *= 1 - decrease;
                }
                else if(test[i][j].energy >= stop){                    // 위의 경우를 제외한 보편적인 경우이고 배열의 에너지가 stop 보다 크거나 같을 경우
                    test[i - 1][j - 1].energy += test[i][j].energy * decrease * side;
                    test[i - 1][j].energy += test[i][j].energy * decrease * top;
                    test[i - 1][j + 1].energy += test[i][j].energy * decrease * side;
                    test[i][j].energy *= 1 - decrease;
                }
                // 배열에 앞으로 나갈 에너지가 부족한 경우는 필요 없으므로 else 를 제외 시
            }
            
            judge(test, i - 1);    // 배열을 보고 판단하여 wet 부분과 dry 부분을 최신화
            
        }
        
    }
    
    static void judge(Water test[][],int place) {    // wet or dry 구분
        
        for(int i = 0; i < size; i++)
            if(test[place][i].energy >= stop)
                test[place][i].state = wet;
        
    }
    
    static boolean keep_going(Water test[][], int place) {    // 계속 가는지 멈췄는지 확인
        
        boolean decision = false;    // 초기 결정 설정
        
        for(int i = 0; i < size; i++) {
            if(test[place][i].state == 1 && test[place][i].energy >= stop) {    // 제일 윗줄에 wet 이고 wet 인 배열의 에너지가 stop 보다 크거나 같은 배열이 하나라도 있으면 true 로 결정 후 break
                decision = true;
                break;
            }
        }
        
        return decision;        // 결정으로 return
    }
    
}
