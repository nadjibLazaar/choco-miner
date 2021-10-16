package core;

import java.util.ArrayList;

import org.chocosolver.solver.constraints.Constraint;

public class Query {

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();

	public boolean add(Constraint... c) {

		for (Constraint cc : c)
			constraints.add(cc);
		return true;
	}

	public boolean remove(Constraint... c) {

		for (Constraint cc : c)
			constraints.remove(cc);
		return true;

	}

	@Override
	public String toString() {
		return "Query [constraints=" + constraints + "]";
	}

}
