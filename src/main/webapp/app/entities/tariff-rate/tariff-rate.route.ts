import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {TariffRateComponent} from "./tariff-rate.component";
import {TariffRateDetailComponent} from "./tariff-rate-detail.component";
import {TariffRatePopupComponent} from "./tariff-rate-dialog.component";
import {TariffRateDeletePopupComponent} from "./tariff-rate-delete-dialog.component";

@Injectable()
export class TariffRateResolvePagingParams implements Resolve<any> {

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

export const tariffRateRoute: Routes = [
	{
		path: 'tariff-rate',
		component: TariffRateComponent,
		resolve: {
			'pagingParams': TariffRateResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.tariffRate.home.title'
		}
	}, {
		path: 'tariff-rate/:id',
		component: TariffRateDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.tariffRate.home.title'
		}
	}
];

export const tariffRatePopupRoute: Routes = [
	{
		path: 'tariff-rate-new',
		component: TariffRatePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.tariffRate.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'tariff-rate/:id/edit',
		component: TariffRatePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.tariffRate.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'tariff-rate/:id/delete',
		component: TariffRateDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.tariffRate.home.title'
		},
		outlet: 'popup'
	}
];
