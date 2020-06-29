



public class MyLinkedQueue implements QueueInterface {

private class QueueNode


{

private Object info;

private QueueNode link;

}

private QueueNode front; 

private QueueNode rear; 

public MyLinkedQueue()


{

front = null;

rear = null;

}

public void enqueue(Object item) {

QueueNode newNode = new QueueNode();

newNode.info = item;

newNode.link = null;

if (rear == null)

front = newNode;

else

rear.link = newNode;

rear = newNode;

}

public Object dequeue(){

Object item;

item = front.info;

front = front.link;

if (front == null)

rear = null;

return item;

}

public boolean isEmpty() {


if (front == null)

return true;

else

return false;

}

public Object peek(){



if (front == null)

return null;

else

return front.info;

}

public int size()


{

QueueNode curr = front;

int count = 0;

while (curr != null) {

++count;

curr = curr.link;

}

return count;

}

public String toString()


{

QueueNode curr = front;

String res = "";

while (curr != null) {

res = res + curr.info + " ";

curr = curr.link;

}

return res;

}

@Override

public boolean isFull() {



return front==null;

}}