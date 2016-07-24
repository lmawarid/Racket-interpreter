
public class Dictionary {
	private class DictNode {
		public String key;
		public double val;
		public DictNode left;
		public DictNode right;
		
		public DictNode(String key, double val) {
			this.key = key;
			this.val = val;
			this.left = null;
			this.right = null;
		}
		
		public void insertNode(String key, double val) {
			DictNode curr = this;
			
			while (curr != null) {
				int cmp = key.compareTo(curr.key);
				
				if (cmp < 0) {
					if (curr.left == null) {
						curr.left = new DictNode(key, val);
						return;
					}
					curr = curr.left;
				} else if (cmp > 0) {
					if (curr.right == null) {
						curr.right = new DictNode(key, val);
						return;
					}
					curr = curr.right;
				} else {
					curr.val = val;
					return;
				}
			}
		}
	}
	
	DictNode root;
	
	public Dictionary() {
		root = null;
	}
	
	public void add(String key, double val) {
		if (root == null) {
			root = new DictNode(key, val);
		} else {
			root.insertNode(key, val);
		}
	}
	
	public double lookup(String key) {
		DictNode curr = root;
		
		while (curr != null) {
			int cmp = key.compareTo(curr.key);
			
			if (cmp < 0) {
				curr = curr.left;
			} else if (cmp > 0) {
				curr = curr.right;
			} else {
				return curr.val;
			}
		}
		
		return Double.MIN_VALUE;
	}
}
