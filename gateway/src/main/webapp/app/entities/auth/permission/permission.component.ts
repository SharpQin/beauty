import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPermission } from '@/shared/model/auth/permission.model';

import PermissionService from './permission.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Permission extends Vue {
  @Inject('permissionService') private permissionService: () => PermissionService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public permissions: IPermission[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPermissions();
  }

  public clear(): void {
    this.retrieveAllPermissions();
  }

  public retrieveAllPermissions(): void {
    this.isFetching = true;
    this.permissionService()
      .retrieve()
      .then(
        res => {
          this.permissions = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IPermission): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePermission(): void {
    this.permissionService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('authApp.authPermission.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPermissions();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
