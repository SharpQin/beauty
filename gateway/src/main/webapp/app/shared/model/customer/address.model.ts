import { ICustomer } from '@/shared/model/customer/customer.model';

export interface IAddress {
  id?: number;
  postalCode?: string | null;
  city?: string | null;
  details?: string;
  defaultAddr?: boolean | null;
  customer?: ICustomer | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public postalCode?: string | null,
    public city?: string | null,
    public details?: string,
    public defaultAddr?: boolean | null,
    public customer?: ICustomer | null
  ) {
    this.defaultAddr = this.defaultAddr ?? false;
  }
}
