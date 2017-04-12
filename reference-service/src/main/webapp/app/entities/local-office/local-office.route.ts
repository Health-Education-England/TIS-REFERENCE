import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {LocalOfficeComponent} from "./local-office.component";
import {LocalOfficeDetailComponent} from "./local-office-detail.component";
import {LocalOfficePopupComponent} from "./local-office-dialog.component";
import {LocalOfficeDeletePopupComponent} from "./local-office-delete-dialog.component";

@Injectable()
export class LocalOfficeResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const localOfficeRoute: Routes = [
	{
		path: 'local-office',
		component: LocalOfficeComponent,
		resolve: {
			'pagingParams': LocalOfficeResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.localOffice.home.title'
		}
	}, {
		path: 'local-office/:id',
		component: LocalOfficeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.localOffice.home.title'
		}
	}
];

export const localOfficePopupRoute: Routes = [
	{
		path: 'local-office-new',
		component: LocalOfficePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.localOffice.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'local-office/:id/edit',
		component: LocalOfficePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.localOffice.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'local-office/:id/delete',
		component: LocalOfficeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.localOffice.home.title'
		},
		outlet: 'popup'
	}
];
