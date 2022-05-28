import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPermission } from '@/shared/model/auth/permission.model';
import PermissionService from './permission.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PermissionDetails extends Vue {
  @Inject('permissionService') private permissionService: () => PermissionService;
  @Inject('alertService') private alertService: () => AlertService;

  public permission: IPermission = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.permissionId) {
        vm.retrievePermission(to.params.permissionId);
      }
    });
  }

  public retrievePermission(permissionId) {
    this.permissionService()
      .find(permissionId)
      .then(res => {
        this.permission = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
