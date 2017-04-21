import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {FundingIssue} from "./funding-issue.model";
import {FundingIssueService} from "./funding-issue.service";

@Component({
	selector: 'jhi-funding-issue-detail',
	templateUrl: './funding-issue-detail.component.html'
})
export class FundingIssueDetailComponent implements OnInit, OnDestroy {

	fundingIssue: FundingIssue;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingIssueService: FundingIssueService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['fundingIssue']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.fundingIssueService.find(id).subscribe(fundingIssue => {
			this.fundingIssue = fundingIssue;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
