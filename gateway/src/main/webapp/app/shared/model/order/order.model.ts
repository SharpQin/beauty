import { IOrderItem } from '@/shared/model/order/order-item.model';

import { OrderStatus } from '@/shared/model/enumerations/order-status.model';
export interface IOrder {
  id?: number;
  serialNo?: string | null;
  price?: number | null;
  status?: OrderStatus | null;
  custId?: number | null;
  createdTime?: Date | null;
  actionTime?: Date | null;
  remark?: string | null;
  items?: IOrderItem[] | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public serialNo?: string | null,
    public price?: number | null,
    public status?: OrderStatus | null,
    public custId?: number | null,
    public createdTime?: Date | null,
    public actionTime?: Date | null,
    public remark?: string | null,
    public items?: IOrderItem[] | null
  ) {}
}
