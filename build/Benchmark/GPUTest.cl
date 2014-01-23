__kernel void factor_test(__global const long* in, int n) 
{
    int id = get_global_id(0);
    if (id >= n)
        return;

	long zuTeilen = in[id];
	int first = 1;
	for (int i = 2; i <= zuTeilen;){
		if (zuTeilen % i == 0){
			if (first == 1){
			} else {
				first = 0;
			}
			zuTeilen = zuTeilen / i;
		}
		if (zuTeilen % i != 0)
			i++;
	}
}
__kernel void sort_test(__global long* in, int n) 
{
    int id = get_global_id(0);
    if (id >= n)
        return;

	for (int j = id + 1; j < n; j++) {
    	if (in[id] > in[j]) {
     		int temp = in[id];
     		in[id] = in[j];
     		in[j] = temp;
    	}
   }
}