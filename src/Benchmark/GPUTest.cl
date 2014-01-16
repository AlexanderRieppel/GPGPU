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