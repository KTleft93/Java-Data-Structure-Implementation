import java.util.*;

public class MyArrayStack {



MyArrayList<String> aList;


MyArrayStack() {

aList = new MyArrayList<String>();

}

public void push(String object) {



aList.add(object);

}

public String pop() {

String ret = null;


if (aList.isEmpty())

System.out.println("Stack is Empty");

else {


ret = aList.get(aList.size() - 1);

aList.remove(aList.size() - 1);

}

return ret;

}

public String peek() {

String ret = null;

if (aList.isEmpty())

System.out.println("Stack is Empty");

else {



ret = aList.get(aList.size() - 1);

}

return ret;

}

public boolean isEmpty() {


return aList.size()==0;

}

public static void main(String[]args) {
	
}


}
