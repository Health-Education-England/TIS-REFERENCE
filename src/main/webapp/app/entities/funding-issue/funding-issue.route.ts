import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {FundingIssueComponent} from "./funding-issue.component";
import {FundingIssueDetailComponent} from "./funding-issue-detail.component";
import {FundingIssuePopupComponent} from "./funding-issue-dialog.component";
import {FundingIssueDeletePopupComponent} from "./funding-issue-delete-dialog.component";

@Injectable()
export class FundingIssueResolvePagingParams implements Resolve<any> {

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

export const fundingIssueRoute: Routes = [
	{
		path: 'funding-issue',
		component: FundingIssueComponent,
		resolve: {
			'pagingParams': FundingIssueResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingIssue.home.title'
		}
	}, {
		path: 'funding-issue/:id',
		component: FundingIssueDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingIssue.home.title'
		}
	}
];

export const fundingIssuePopupRoute: Routes = [
	{
		path: 'funding-issue-new',
		component: FundingIssuePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingIssue.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'funding-issue/:id/edit',
		component: FundingIssuePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingIssue.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'funding-issue/:id/delete',
		component: FundingIssueDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.fundingIssue.home.title'
		},
		outlet: 'popup'
	}
];
