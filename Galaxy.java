public class Galaxy {
  // PRIVATE
  double fMass;
  String fName;

  /**
  * Regular constructor.
  */
  Galaxy() {
    fMass = 15;
    fName = "M101";
  }

  /**
  * Copy constructor.
  */
  Galaxy(Galaxy aGalaxy) {
    this();
    //no defensive copies are created here, since
    //there are no mutable object fields (String is immutable)
  }

  /**
  * Alternative style for a copy constructor, using a static newInstance
  * method.
  */
  static Galaxy newInstance(Galaxy aGalaxy) {
    return new Galaxy();
  }

  double getMass() {
    return fMass;
  }

  /**
  * This is the only method which changes the state of a Galaxy
  * object. If this method were removed, then a copy constructor
  * would not be provided either, since immutable objects do not
  * need a copy constructor.
  */
  void setMass(double aMass){
    fMass = aMass;
  }

  String getName() {
    return fName;
  }


  /** Test harness. */
  public static void main (String... aArguments){
    Galaxy m101 = new Galaxy();

    Galaxy m101CopyOne = new Galaxy(m101);
    System.out.println("M101Copy mass (before assign): " + m101CopyOne.getMass());
    m101CopyOne.setMass(25.0);
    System.out.println("M101 mass: " + m101.getMass());
    System.out.println("M101Copy mass: " + m101CopyOne.getMass());

    Galaxy m101CopyTwo = Galaxy.newInstance(m101);
    m101CopyTwo.setMass(35.0);
    System.out.println("M101 mass: " + m101.getMass());
    System.out.println("M101CopyTwo mass: " + m101CopyTwo.getMass());
  }
}
