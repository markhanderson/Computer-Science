#ifndef _cmpfn_h
#define _cmpfn_h
template <typename Type>
int OperatorCmp(Type one, Type two) {
	if (one == two) return 0;
	if (one < two) return -1;
	return 1;
}
#endif
