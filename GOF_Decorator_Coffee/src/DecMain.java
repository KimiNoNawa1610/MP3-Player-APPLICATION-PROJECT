
public class DecMain {
	static public void main(String [] args){
		Beverage	b = new Expresso();
	
		System.out.print(b.getDesc()); System.out.print("    $"); System.out.println(b.cost());
	
	
		b = new Mocha(b);
  
		System.out.print(b.getDesc()); System.out.print("    $"); System.out.println(b.cost());
		b = new Whip(b);
		System.out.print(b.getDesc()); System.out.print("    $"); System.out.println(b.cost());
  
		Beverage	b2 = new Houseblend();
		b2 = new Mocha(b2);
	    b2 = new Whip(b2);
	    b2 = new Mocha(b2);
	    System.out.print(b2.getDesc()); System.out.print("    $"); System.out.println(b2.cost());
   
}
}
