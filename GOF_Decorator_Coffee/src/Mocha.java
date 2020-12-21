
public class Mocha extends CondimentDecorator {
	Beverage beverage;
    public	Mocha(Beverage bev) {
		beverage = bev;
	}
	public String  getDesc() {
		return beverage.getDesc() + ", Mocha";
	}
	public  double cost() {
		return .20 + beverage.cost();
	}
}
