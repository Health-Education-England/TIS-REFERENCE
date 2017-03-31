import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {RecordType} from "./record-type.model";
import {RecordTypeService} from "./record-type.service";

@Component({
	selector: 'jhi-record-type-detail',
	templateUrl: './record-type-detail.component.html'
})
export class RecordTypeDetailComponent implements OnInit, OnDestroy {

	recordType: RecordType;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private recordTypeService: RecordTypeService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['recordType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.recordTypeService.find(id).subscribe(recordType => {
			this.recordType = recordType;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
