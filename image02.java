package Image;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class image02 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {

			Mat source = Imgcodecs.imread("C://opencv3.1//10.jpg");
			Mat dst = new Mat(source.rows(), source.cols(), source.type());
			Imgproc.cvtColor(source, dst, Imgproc.COLOR_BGR2GRAY);
			Imgcodecs.imwrite("C://opencv3.1//10-1.jpg", dst);
			List<Mat> bgrList = new ArrayList<Mat>(3);

			System.out.println("channels=" + source.channels());// 灰階=1,RGB=3
			System.out.println("total=" + source.total());// col*row
			System.out.println("col=" + source.cols());// col*row
			System.out.println("row=" + source.rows());// col*row
			System.out.println("size=" + source.size());// cols*rows,ex:9X10
			System.out.println("depth=" + source.depth());// 0
			System.out.println("type=" + source.type());// 16
			System.out.println("source=" + source.dump());// (BGR,BGR......9組,共9x3=27(col),row=10)
			
			 Core.split(source, bgrList);// split 3 channels,R(2),G(1),B(0)
			 System.out.println("blue=" + bgrList.get(0).dump());
			 System.out.println("green=" + bgrList.get(1).dump());
			 System.out.println("red=" + bgrList.get(2).dump());

			// System.out.println("channels=" + dst.channels());// 灰階=1,RGB=3
			// System.out.println("total=" + dst.total());// col*row
			// System.out.println("col=" + dst.cols());// col*row
			// System.out.println("row=" + dst.rows());// col*row
			// System.out.println("size=" + dst.size());// cols*rows,ex:9X10
			// System.out.println("depth=" + dst.depth());// 0
			// System.out.println("type=" + dst.type());// 16
			System.out.println("gray=" + dst.dump());// (BGR,BGR......9組,共9x3=27(col),row=10)
			//
			// Core.split(dst, bgrList);// split 3 channels,R(2),G(1),B(0)
			// System.out.println("blue=" + bgrList.get(0).dump());// get only
			// blue
			// // channel mat
			// System.out.println("green=" + bgrList.get(1).dump());// get only
			// // blue
			// // channel
			// // mat
			// System.out.println("red=" + bgrList.get(2).dump());// get only
			// blue
			// // channel mat

			// Mat rev = dst.reshape((int) dst.total(), 1);
			// System.out.println("m=" + rev.dump());
			// Mat mm = new Mat();
			// Core.add(dst, dst, mm);
			// System.out.println(mm.dump());

			Mat diff = new Mat(source.rows(), source.cols() - 1, source.type());
			for (int j = 0; j < source.rows(); j++) {
				for (int i = 0; i < source.cols() - 1; i++) {
					double[] temp1 = source.get(j, i);
					double[] temp2 = source.get(j, i + 1);

					if (temp2[0] >= temp1[0]) {
						temp1[0] = temp2[0] - temp1[0];
					} else {
						temp1[0] = temp1[0] - temp2[0];
					}
					if (temp2[1] >= temp1[1]) {
						temp1[1] = temp2[1] - temp1[1];
					} else {
						temp1[1] = temp1[1] - temp2[1];
					}
					if (temp2[2] >= temp1[2]) {
						temp1[2] = temp2[2] - temp1[2];
					} else {
						temp1[2] = temp1[2] - temp2[2];
					}
					diff.put(j, i, temp1);
				}
			}
			System.out.println("diff=" + diff.dump());// (BGR,BGR......9組,共9x3=27(col),row=10)

			dst = new Mat(diff.rows(), diff.cols(), diff.type());
			Imgproc.cvtColor(diff, dst, Imgproc.COLOR_BGR2GRAY);
			Imgcodecs.imwrite("C://opencv3.1//10-1.jpg", dst);
			System.out.println("dst=" + dst.dump());// (BGR,BGR......9組,共9x3=27(col),row=10)

			Mat qan = new Mat(dst.rows(), dst.cols(), dst.type());
			Mat adj = new Mat(dst.rows(), dst.cols(), dst.type());
			// Mat two = new Mat(1, 1, dst.type(), new Scalar(2));
			// Mat five = new Mat(1, 1, dst.type(), new Scalar(5));
			for (int j = 0; j < dst.rows(); j++) {
				for (int i = 0; i < dst.cols(); i++) {
					double[] temp3 = dst.get(j, i);
					double[] temp4 = dst.get(j, i);
					double[] temp5 = dst.get(j, i);
					// double[] temp6 = two.get(0, 0);
					// double[] temp7 = five.get(0, 0);

					// int A = 0;
					// while (temp3[0] <= A) {
					// A = A + 5;
					// }

					temp4[0] = (int) (temp3[0] / 5);
					temp5[0] = temp3[0] % 5;
					if (temp5[0] > 2) {
						temp4[0] = (temp4[0] + 1) * 5;
						temp5[0] = 5 - temp5[0];
					} else {
						temp4[0] = temp4[0] * 5;
						// temp5[0] = temp5[0] - 5;
					}

					qan.put(j, i, temp4);
					adj.put(j, i, temp5);
				}
			}
			// System.out.println("five=" + five.dump());//
			// (BGR,BGR......9組,共9x3=27(col),row=10)
			System.out.println("qan=" + qan.dump());// (BGR,BGR......9組,共9x3=27(col),row=10)
			System.out.println("adj=" + adj.dump());// (BGR,BGR......9組,共9x3=27(col),row=10)

			// Mat rev1 = new Mat(rev.rows()-1, rev.cols(), rev.type());
			// Mat temp = new Mat(4, 7, CvType.CV_32FC1);

			// for (int i = 0; i < temp.rows(); i++) {
			// for (int j = 0; j < temp.cols(); j++) {
			//// rev1.put(i, 1, rev.get(i, 1, data))
			// double[] temp1 = temp.get(i, j);
			//// double[] temp1 = rev.get(rev.rows()+1, 1);
			//// temp1[0]=temp[1]-temp[0];
			//// rev1.put(i, 1, temp[1]-temp[0]);
			// System.out.println(temp.get(i, j));
			// }
			////
			// }
			// System.out.println("m=" + rev1.dump());

			// Mat dif1 = new Mat(source.rows(), source.cols(), source.type());
			// for (int i = 0; i < source.rows(); i++) {
			// for (int j = 0; j < source.cols(); j++) {
			// dif1.put(i, j, source.get(i, j));
			// }
			// }
			// System.out.println("m=" + dif1.dump());

			// int size = (int)source.total() * source.channels();
			// byte[] da = new byte[size];
			// qan.get(0, 0, da);
			// System.out.println("data=" + da);
			// System.out.println("source=" + source.dump());

		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
		}
	}

}