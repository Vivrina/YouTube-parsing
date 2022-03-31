import java.util.Comparator;

class MyComparator implements Comparator<Link> {

    @Override
    public int compare(Link o1, Link o2) {
        return Integer.compare(o2.getCount(), o1.getCount());
    }

}

