export interface IMenu {
  id?: number;
  name?: string;
  title?: string;
  link?: string;
  method?: string;
  authKey?: string;
  showed?: boolean | null;
  parent?: number | null;
}

export class Menu implements IMenu {
  constructor(
    public id?: number,
    public name?: string,
    public title?: string,
    public link?: string,
    public method?: string,
    public authKey?: string,
    public showed?: boolean | null,
    public parent?: number | null
  ) {
    this.showed = this.showed ?? false;
  }
}
