import java.util.Arrays;

public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     }

     // write your codes here
     static class Node {
          int value;
          Node left;
          Node right;

          Node(int value) {
               this.value = value;
          }
     }

     public static Node insert(Node root, int value) {
          if (root == null) {
               return new Node(value);
          }
          if (value < root.value) {
               root.left = insert(root.left, value);
          } else {
               root.right = insert(root.right, value);
          }
          return root;
     }

     public static void collectPostorder(Node node, int[] out, int[] index) {
          if (node == null) {
               return;
          }
          collectPostorder(node.left, out, index); // left
          collectPostorder(node.right, out, index); // right
          out[index[0]++] = node.value; // root last
     }

     public final static int[] arraySplit(String inputString) {
          return Arrays.stream(inputString.split("-"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
     }

     public static boolean sameArray(int[] first, int[] second) {
          if (first.length != second.length) {
               return false;
          }

          for (int i = 0; i < first.length; i++) {
               if (first[i] != second[i]) {
                    return false;
               }
          }

          return true;
     }

     public static boolean isPreorder(int[] array) {
          int[] stack = new int[array.length];
          int top = -1;
          int root = Integer.MIN_VALUE;

          for (int value : array) {
               if (value <= root) {
                    return false;
               }

               // if right side of root, value will be bigger
               while (top >= 0 && value > stack[top]) {
                    root = stack[top];
                    top--;
               }

               stack[++top] = value;
          }

          return true;
     }

     // build the BST from the preorder, then compare its postorder with the
     // candidate
     public static boolean isPostorder(int[] preorder, int[] candidate) {
          Node root = null;
          for (int value : preorder) {
               root = insert(root, value);
          }
          int[] result = new int[preorder.length];
          collectPostorder(root, result, new int[1]);
          return sameArray(result, candidate);
     }

     public static String verify(String traversal1, String traversal2, String traversal3) {

          int[] t1 = arraySplit(traversal1);
          int[] t2 = arraySplit(traversal2);
          int[] t3 = arraySplit(traversal3);

          if (t1.length != t2.length || t1.length != t3.length) {
               return "Invalid traversals";
          }

          int[] sorted = Arrays.copyOf(t1, t1.length);
          Arrays.sort(sorted);

          // inOrder check: Nodes appearing in ascending order
          boolean inOrder_t1 = sameArray(t1, sorted);
          boolean inOrder_t2 = sameArray(t2, sorted);
          boolean inOrder_t3 = sameArray(t3, sorted);

          if (!inOrder_t1 && !inOrder_t2 && !inOrder_t3) {
               return "Invalid traversals";
          }

          // identify pre-order: root first
          boolean preOrder_t1 = !inOrder_t1 && isPreorder(t1);
          boolean preOrder_t2 = !inOrder_t2 && isPreorder(t2);
          boolean preOrder_t3 = !inOrder_t3 && isPreorder(t3);

          if (!preOrder_t1 && !preOrder_t2 && !preOrder_t3) {
               return "Invalid traversals";
          }

          // postOrder check: the leftover traversal must equal the postorder
          // of the tree built from the preorder
          int[][] t = { t1, t2, t3 };
          boolean[] isIn = { inOrder_t1, inOrder_t2, inOrder_t3 };
          boolean[] isPre = { preOrder_t1, preOrder_t2, preOrder_t3 };

          for (int in = 0; in < 3; in++) {
               if (!isIn[in])
                    continue;

               for (int pre = 0; pre < 3; pre++) {
                    if (pre == in || !isPre[pre])
                         continue;

                    int post = 3 - in - pre; // the index left over (0 + 1 + 2 = 3)

                    if (isPostorder(t[pre], t[post])) {
                         String[] label = new String[3];
                         label[in] = "Inorder";
                         label[pre] = "Preorder";
                         label[post] = "Postorder";
                         return "Traversal 1 - " + label[0] + ", Traversal 2 - " + label[1]
                                   + ", Traversal 3 - " + label[2];
                    }
               }
          }

          return "Invalid traversals";
     }
}
