
public class Whip extends CondimentDecorator {
	Beverage	beverage;
    public	Whip(Beverage bev) {
		beverage = bev;
	}
	public String getDesc() {
		return beverage.getDesc() + ", Whip";
	}
	public double cost() {
		return .15 + beverage.cost();
	}
}
