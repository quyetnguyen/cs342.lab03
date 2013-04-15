public class TreeObject<T> implements Comparable<TreeObject>
{
	public T data;
	public int frequency;


	public TreeObject (T data)
	{
		this.data = data;
		frequency = 1;
	}

	public int getKey()
	{
		if (data.getClass().equals(long.class)) return (Integer) data;
		else
			return Math.abs(data.hashCode());
	}

	public int compareTo(TreeObject treeObject)
	{
		if (this.data.equals(treeObject.data)) return 0;

		return 1;
	}

	public void incrementFrequency()
	{
		frequency++;
	}

	public int getFrequency()
	{
		return frequency;
	}

	public String toString()
	{
		return data.toString();
	}

}