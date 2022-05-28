import { IOrder } from '@/shared/model/order/order.model';

import { OrderItemStatus } from '@/shared/model/enumerations/order-item-status.model';
export interface IOrderItem {
  id?: number;
  prodId?: number;
  quantity?: number;
  totalPrice?: number;
  price?: number;
  status?: OrderItemStatus;
  order?: IOrder | null;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public prodId?: number,
    public quantity?: number,
    public totalPrice?: number,
    public price?: number,
    public status?: OrderItemStatus,
    public order?: IOrder | null
  ) {}
}
