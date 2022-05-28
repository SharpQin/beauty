import { ICustomer } from '@/shared/model/customer/customer.model';

import { AccountType } from '@/shared/model/enumerations/account-type.model';
export interface ICusAccount {
  id?: number;
  name?: string;
  cardNo?: string;
  accountType?: AccountType | null;
  customer?: ICustomer | null;
}

export class CusAccount implements ICusAccount {
  constructor(
    public id?: number,
    public name?: string,
    public cardNo?: string,
    public accountType?: AccountType | null,
    public customer?: ICustomer | null
  ) {}
}
