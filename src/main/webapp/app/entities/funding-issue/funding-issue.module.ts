import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	FundingIssueService,
	FundingIssuePopupService,
	FundingIssueComponent,
	FundingIssueDetailComponent,
	FundingIssueDialogComponent,
	FundingIssuePopupComponent,
	FundingIssueDeletePopupComponent,
	FundingIssueDeleteDialogComponent,
	fundingIssueRoute,
	fundingIssuePopupRoute,
	FundingIssueResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...fundingIssueRoute,
	...fundingIssuePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		FundingIssueComponent,
		FundingIssueDetailComponent,
		FundingIssueDialogComponent,
		FundingIssueDeleteDialogComponent,
		FundingIssuePopupComponent,
		FundingIssueDeletePopupComponent,
	],
	entryComponents: [
		FundingIssueComponent,
		FundingIssueDialogComponent,
		FundingIssuePopupComponent,
		FundingIssueDeleteDialogComponent,
		FundingIssueDeletePopupComponent,
	],
	providers: [
		FundingIssueService,
		FundingIssuePopupService,
		FundingIssueResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceFundingIssueModule {
}
