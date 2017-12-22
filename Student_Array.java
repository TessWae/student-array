import javax.swing.*;

public class Foothill
{

   public static void main (String[] args)
   {

      int k;
      Student student;

      Student[] myClass = { new Student("Guy", "Some", 76), 
            new Student(“Eriksson”, “Erik”, 51),
            new Student(“Larsson”, “Lars”, 91), 
            new Student(“Andersson”, “Anders”, 86),
            new Student(“Markusson”, “Markus”, 100), 
            new Student(“Oskarsson”, “Oskar”, 63),
            new Student(“Thomasson”, “Thomas”, 97), 
            new Student(“Bjornsson”, “Bjorn”, 87),
            new Student(“Persson”,”Per”, 84), 
            new Student(“Johnsson”, “John”, 98),
            new Student("Smoe", "Joe", 74),
            new Student("MrSir", "Zach", 99),
            new Student(“Goransson”, “Goran”, 91),
            new Student(“Alexandersson”, "Alexander”, 76),
      };

      // instantiate a StudArrUtilObject
      StudentArrayUtilities myStuds = new StudentArrayUtilities();

      // we can add students manually and individually
      myStuds.addStudent( new Student("Rock", "Bradley", 79) );
      myStuds.addStudent( new Student("Red", "Paulina", 72) );

      // if we happen to have an array available, we can add students in loop.
      for (k = 0; k < myClass.length; k++)
         myStuds.addStudent( myClass[k] );

      System.out.println( myStuds.toString("Before: "));

      myStuds.arraySort();
      System.out.println( myStuds.toString("Default Sort: "));

      Student.setSortKey(Student.SORT_BY_FIRST);
      myStuds.arraySort();
      System.out.println( myStuds.toString("First Name Sort: "));

      Student.setSortKey(Student.SORT_BY_POINTS);
      myStuds.arraySort();
      System.out.println( myStuds.toString("Sorting by total points: "));

      //median calculated
      System.out.println("Median of class: "
            + myStuds.getMedianDestructive() + "\n");

      // various tests of removing and adding too many students
      for (k = 0; k < 100; k++)
      {
         if ( (student = myStuds.removeStudent()) != null)
            System.out.println("Removed " + student);
         else
         {
            System.out.println("Empty after " + k + " students are removed.");
            break;
         }
      }

      for (k = 0; k < 100; k++)
      {
         if (!myStuds.addStudent(new Student("first", "last", 22)))
         {
            System.out.println("Full after " + k + " students are added.");
            break;
         }
      }
   }
}

class Student
{

   private String lastName;
   private String firstName;
   private int totalPoints;

   public static final String DEFAULT_NAME = "zz-error";
   public static final int DEFAULT_POINTS = 0;
   public static final int MAX_POINTS = 1000;

   public static final int SORT_BY_FIRST = 88;
   public static final int SORT_BY_LAST = 98;
   public static final int SORT_BY_POINTS = 108;

   private static int sortKey = SORT_BY_LAST;

   // constructor
   public Student( String last, String first, int points)
   {
      if ( !setLastName(last) )
         lastName = DEFAULT_NAME;
      if ( !setFirstName(first) )
         firstName = DEFAULT_NAME;
      if ( !setPoints(points) )
         totalPoints = DEFAULT_POINTS;
   }

   public String getLastName() { return lastName; }
   public String getFirstName() { return firstName; }
   public int getTotalPoints() { return totalPoints; }

   //setter and getter for sortkey
   public static boolean setSortKey(int key)
   {
      if (key == SORT_BY_FIRST)
      {
         sortKey = key;
         return true;
      }
      else if (key == SORT_BY_LAST)
      {
         sortKey = key;
         return true;
      }
      else if (key == SORT_BY_POINTS)
      {
         sortKey = key;
         return true;
      }
      else
         return false;
   }

   public static int getSortKey()
   {
      return sortKey;
   }

   //setters
   public boolean setLastName(String last)
   {
      if ( !validString(last) )
         return false;
      lastName = last;
      return true;
   }

   public boolean setFirstName(String first)
   {
      if ( !validString(first) )
         return false;
      firstName = first;
      return true;
   }

   public boolean setPoints(int pts)
   {
      if ( !validPoints(pts) )
         return false;
      totalPoints = pts;
      return true;
   }

   //comparing students
   public static int compareTwoStudents( Student firstStud, 
         Student secondStud )
   {
      int result;

      switch (sortKey)
      {
      case SORT_BY_POINTS:
         result = firstStud.totalPoints - secondStud.totalPoints;
         break;
      case SORT_BY_FIRST:
         result = 
         firstStud.firstName.compareToIgnoreCase(secondStud.firstName);
         break;
      default:
         result = 
         firstStud.lastName.compareToIgnoreCase(secondStud.lastName);
         break;
      }
      return result;
   }

   public String toString()
   {
      String resultString;

      resultString = " "+ lastName
            + ", " + firstName
            + " points: " + totalPoints
            + "\n";
      return resultString;
   }

   private static boolean validString( String testStr )
   {
      if (testStr != null && Character.isLetter(testStr.charAt(0)))
         return true;
      return false;
   }

   private static boolean validPoints( int testPoints )
   {
      if (testPoints >= 0 && testPoints <= MAX_POINTS)
         return true;
      return false;
   }
}

class StudentArrayUtilities
{
   public static final int MAX_STUDENTS = 20;

   private int numStudents = 0;
   private Student[] array = new Student[MAX_STUDENTS];

   public String toString( String title )
   {
      String output = title + "\n";
      for (int k = 0; k < numStudents; k++)
         output += " "+ array[k].toString();
      return output;
   }

   public boolean addStudent(Student stud)
   {
      if (stud == null || numStudents >= MAX_STUDENTS)
         return false;
      array[numStudents] = stud;
      numStudents++;
      return true;
   }

   public Student removeStudent()
   {
      Student lastStudent;

      if (numStudents == 0)
         return null;
      lastStudent = array[numStudents - 1];
      numStudents--;
      return lastStudent;
   }

   public double getMedianDestructive()
   {
      double median;

      if (numStudents == 0)
         return 0;
      if (numStudents == 1)
         return array[0].getTotalPoints();

      int oldKey = Student.getSortKey();
      Student.setSortKey(Student.SORT_BY_POINTS);
      arraySort();

      if (numStudents % 2 == 1)
         median = array[numStudents / 2].getTotalPoints();
      else
         median = (array[numStudents / 2 - 1].getTotalPoints() +
               array[numStudents / 2].getTotalPoints()) / 2.0;

      Student.setSortKey(oldKey);
      return median;
   }

   // returns true if a modification was made to the array
   private boolean floatLargestToTop(int top)
   {
      boolean changed = false;
      Student temp;

      // compare with client call to see where the loop stops
      for (int k = 0; k < top; k++)
         if ( Student.compareTwoStudents(array[k], array[k+1]) > 0 )
         {
            temp = array[k];
            array[k] = array[k+1];
            array[k+1] = temp;
            changed = true;
         }
      return changed;
   }

   // public callable arraySort
   public void arraySort()
   {
      for (int k = 0; k < numStudents; k++)
         if ( !floatLargestToTop(numStudents -1-k) )
            return;
   }
}
