import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Status} from "./status.model";
import {StatusService} from "./status.service";

@Component({
	selector: 'jhi-status-detail',
	templateUrl: './status-detail.component.html'
})
export class StatusDetailComponent implements OnInit, OnDestroy {

	status: Status;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private statusService: StatusService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['status']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.statusService.find(id).subscribe(status => {
			this.status = status;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
