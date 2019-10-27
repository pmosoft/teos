
public class ArrayTest {

	public static void main(String[] args) {
		int arr2[][] = new int[10][5];
		int arr1[] = new int[arr2.length * arr2[0].length];
		// 배열에 값을 넣는다.
		for (int i = 0; i < arr2.length; i++) {
			for (int j = 0; j < arr2[i].length; j++) {
				arr2[i][j] = (i + 1) * (j + 1);
			}
		}
		
		System.out.println("2차원 배열");
		// 2차원 배열 출력
		for (int i = 0; i < arr2.length; i++) {
			for (int j = 0; j < arr2[i].length; j++) {
				System.out.print("\t" + arr2[i][j]);
			}
			System.out.println("\n");
		}
		
		System.out.println("변환");
		
		for (int i = 0; i < arr2.length; i++) {
			for (int j = 0; j < arr2[i].length; j++) {
				// 2차원 배열의 원소를 1차원 배열의 원소로 이동.
				arr1[(i * arr2[i].length) + j] = arr2[i][j];
			}
		}
		
		// 1차원 배열 출력
		System.out.println("1차원 배열");
		for (int i = 0; i < arr1.length; i++) {
			if (i % arr2[0].length == 0) {
				System.out.println();
			}
			System.out.print("\t" + arr1[i]);
		}
	}

}
