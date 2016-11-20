package hap.event.timed;

import java.util.Comparator;

public class TimedComparator implements Comparator<TimedEvent>
{
@Override
public int compare( TimedEvent o1, TimedEvent o2 )
{
	return o1.getInstant().compareTo( o2.getInstant() );
}
}